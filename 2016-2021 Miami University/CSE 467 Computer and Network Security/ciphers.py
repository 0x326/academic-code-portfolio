#!/usr/bin/env python3

import math
import logging
from itertools import chain, filterfalse, cycle
from typing import Iterable, NamedTuple, Sequence, List, TypeVar, Union


def shift_char(char: str, shift: int) -> str:
    code_point = ord(char.upper())
    code_point -= ord('A')
    code_point = (code_point + shift) % 25
    code_point += ord('A')
    return chr(code_point)


def caesar_cipher(text: str, key: int, reverse=False) -> str:
    if reverse:
        key *= -1

    text = text.upper()
    return ''.join(shift_char(letter, key) for letter in text)


def vigenere_cipher(text: str, key: Iterable[int], reverse=False) -> str:
    if reverse:
        key = (key_part * -1 for key_part in key)

    text = text.upper()
    return ''.join(shift_char(letter, key) for letter, key in zip(text, cycle(key)))


def one_time_pad(text: bytes, key: bytes) -> bytes:
    return bytes(byte ^ key for byte, key in zip(text, key))


class BitString(List[bool]):
    def __init__(self, value: Union[int, Iterable[str], Iterable[bool], Iterable[int]] = (), word_size: int = 8):
        super().__init__()
        try:
            value = iter(value)
        except TypeError:
            value = (value,)

        for val in value:
            try:
                assert not isinstance(val, bool)
                binary_repr = f'{val:b}'
            except (TypeError, ValueError, AssertionError):
                self.append(bool(val) and val != '0')
            else:
                zeros_missing = (word_size - (len(binary_repr) % word_size)) % word_size
                val = ('0' * zeros_missing) + binary_repr
                self.extend(map(lambda item: bool(item) and item != '0', val))

        # self.extend(map(lambda item: bool(item) and item != '0', value))
        self.word_size = word_size

    def __getitem__(self, item):
        item_value = super().__getitem__(item)
        if isinstance(item, slice):
            return BitString(item_value)
        else:
            return item_value

    def __int__(self):
        bit_string = str(self).replace(' ', '')
        return int(f'0b{bit_string}', base=0)

    def __xor__(self, other: 'BitString'):
        return BitString(bit ^ other_bit for bit, other_bit in zip(self, other))

    def __str__(self) -> str:
        bit_string = ''.join('1' if bit else '0' for bit in self)
        return ' '.join(divide_into_chunks(bit_string, chunk_size=self.word_size))


class HexString(str):
    def __new__(cls, value: Union[str, bytes, Iterable[bool]] = '', word_size: int = 8):
        try:
            hexadecimal = value.hex()
        except AttributeError:
            try:
                binary_string = '' .join(filterfalse(lambda str_part: '0' <= str_part <= '1', value))
                value = int(f'0b{binary_string}', base=0)
            except TypeError:
                value = int(value)

            hexadecimal = value.to_bytes(8, 'big').hex()

        cls.word_size = word_size
        return super().__new__(cls, hexadecimal)

    def __str__(self):
        hexadecimal = super().__str__()
        return ' '.join(divide_into_chunks(hexadecimal, chunk_size=math.ceil(self.word_size / 4)))


class DESBlock(NamedTuple):
    left_subblock: BitString
    right_subblock: BitString


des_initial_permutation_table = \
    tuple(chain(*(range(57 + offset, -1 + offset, -8)
                  for offset in chain(range(0, 7, 2), range(-1, 7, 2)))))

des_final_permutation_table, _ = zip(*sorted(enumerate(des_initial_permutation_table),
                                             key=lambda item: item[1]))

des_s_box_tables = (
    ((14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7),
     (0, 15, 7, 4, 14, 2, 13, 10, 3, 6, 12, 11, 9, 5, 3, 8),
     (4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0),
     (15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13)),
)

des_p_box_table = (
    15, 6, 19, 20, 28, 11, 27, 16,
    0, 14, 22, 25, 4, 17, 30, 9,
    1, 7, 23, 13, 31, 26, 2, 8,
    18, 12, 29, 5, 21, 10, 3, 24,
)


def des_permutation(block: BitString, permutation_table: Iterable[int]) -> BitString:
    return BitString(block[permutation_index] for permutation_index in permutation_table)


T = TypeVar('T')


def divide_into_chunks(seq: T, chunk_size: int = 4) -> Iterable[T]:
    copy_index = 0
    while copy_index < len(seq):
        yield seq[copy_index: copy_index + chunk_size]
        copy_index += chunk_size


def des_expansion_d_box(block: BitString) -> BitString:
    # Break into 4-bit chunks
    sub_blocks = list(divide_into_chunks(block, chunk_size=4))
    expanded_blocks = []

    # Expand 4-bits to 6-bits
    for index, sub_block in enumerate(sub_blocks):
        expanded_blocks.append([
            # Insert last value from previous chunk
            sub_blocks[index - 1][-1],
            *sub_block,
            # Append first value from next chunk
            sub_blocks[(index + 1) % len(sub_blocks)][0]])

    # Combine
    return BitString(chain(*expanded_blocks))


def des_s_box(block: BitString, substitution_table: Sequence[Sequence[int]]) -> BitString:
    new_blocks = []

    sub_blocks: List[BitString] = list(divide_into_chunks(block, chunk_size=6))
    for first_bit, *four_bits, last_bit in sub_blocks:
        row_index = int(BitString((first_bit, last_bit)))
        column_index = int(BitString(four_bits))
        new_bits_value = substitution_table[row_index][column_index]
        new_blocks.extend(BitString(new_bits_value, word_size=4))

    return BitString(new_blocks)


def des_p_box(block: BitString) -> BitString:
    return des_permutation(block, des_p_box_table)


def des_feistel_cipher(block: DESBlock, key: BitString, round_number: int = 1) -> DESBlock:
    left_block, right_block = block

    assert len(left_block) == 32
    assert len(right_block) == 32
    assert len(key) == 48

    logging.debug(f'Beginning Feistel cipher (Round {round_number})')
    logging.debug(f'[Left  32 bits]: {left_block}')
    logging.debug(f'[Right 32 bits]: {right_block}')
    next_right_block = des_expansion_d_box(right_block)
    logging.debug(
        f'[Right 32 bits] Expansion d-box: {next_right_block}')
    next_right_block ^= key
    logging.debug(f'[Right 32 bits] Key:             {key}')
    logging.debug(
        f'[Right 32 bits] XOR with Key:    {next_right_block}')
    next_right_block = des_s_box(next_right_block, des_s_box_tables[round_number - 1])
    logging.debug(
        f'[Right 32 bits] S-Box:           {next_right_block}')
    next_right_block = des_p_box(next_right_block)
    logging.debug(
        f'[Right 32 bits] P-Box:           {next_right_block}')
    next_right_block ^= left_block
    logging.debug(
        f'[Right 32 bits] XOR with Left:   {next_right_block}')

    return DESBlock(right_block, next_right_block)


def des_cipher_block(block: BitString, key: BitString) -> BitString:
    logging.debug(f'Initial input:       {block}')

    # Initial permutation
    block = des_permutation(block, des_initial_permutation_table)
    logging.debug(f'Initial permutation: {block}')

    # Feistel rounds
    block_halves = DESBlock(block[:32], block[32:])
    block_halves = des_feistel_cipher(block_halves, key)

    # Join
    block = BitString(chain(*block_halves))
    logging.debug(f'Join:                {block}')

    # Final permutation
    block = des_permutation(block, des_final_permutation_table)
    logging.debug(f'Final permutation:   {block}')
    return block


def des_cipher(data: BitString, key: BitString) -> BitString:
    return BitString(chain(*(des_cipher_block(block, key) for block in divide_into_chunks(data, chunk_size=64))))
