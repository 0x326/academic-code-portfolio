#!/usr/bin/env python3

import sys
from collections import Counter, OrderedDict
from itertools import islice

from typing import Mapping, Dict

from ciphers import *


def roundrobin(*iterables):
    """
    roundrobin('ABC', 'D', 'EF') --> A D E B F C

    Borrowed from Python itertools docs
    """
    # Recipe credited to George Sakkis
    num_active = len(iterables)
    nexts = cycle(iter(it).__next__ for it in iterables)
    while num_active:
        try:
            for next in nexts:
                yield next()
        except StopIteration:
            # Remove the iterator we just exhausted from the cycle.
            num_active -= 1
            nexts = cycle(islice(nexts, num_active))


def frequency_analysis(ciphertext: str, expected_frequencies: Mapping[str, float]) -> Dict[str, str]:
    ciphertext_character_counts: Counter[str] = Counter(ciphertext)
    ciphertext_frequencies = OrderedDict((letter, count / len(ciphertext))
                                         for letter, count in ciphertext_character_counts.most_common())

    expected_frequencies = sorted(expected_frequencies.items(), key=lambda item: item[1], reverse=True)

    decryption_function: Dict[str, str] = {ciphertext_letter: plaintext_letter
                                           for (ciphertext_letter, _), (plaintext_letter, _) in
                                           zip(ciphertext_frequencies.items(), expected_frequencies)}
    return decryption_function


def question2():
    ciphertext = 'UXGPOGZCFJZJTFADADAJEJNDZMZHBBGZGGKQGVVGXCDIWGX'

    expected_frequencies = {
        'A': 0.08167,
        'B': 0.01492,
        'C': 0.02782,
        'D': 0.04253,
        'E': 0.12702,
        'F': 0.02280,
        'G': 0.02015,
        'H': 0.06094,
        'I': 0.06966,
        'J': 0.01530,
        'K': 0.07720,
        'L': 0.04025,
        'M': 0.02406,
        'N': 0.06749,
        'O': 0.07507,
        'P': 0.01929,
        'Q': 0.00950,
        'R': 0.05987,
        'S': 0.06327,
        'T': 0.09056,
        'U': 0.02758,
        'V': 0.00978,
        'W': 0.02360,
        'X': 0.00150,
        'Y': 0.01974,
        'Z': 0.00074,
    }

    # Substitution cipher frequency analysis
    decryption_function = frequency_analysis(ciphertext, expected_frequencies)
    plaintext = ''.join(map(decryption_function.get, ciphertext))
    print(f'Substitution cipher: \t{plaintext}')

    # Vingenere cipher frequency analysis
    for key_length in range(2, len(ciphertext)):
        ciphertext_parts = [ciphertext[vigenere_key_index::key_length] for vigenere_key_index in range(key_length)]
        decryption_functions = [frequency_analysis(ciphertext_part, expected_frequencies)
                                for ciphertext_part in ciphertext_parts]
        plaintext_parts = [''.join(map(decryption_function.get, ciphertext_part))
                           for ciphertext_part, decryption_function in zip(ciphertext_parts, decryption_functions)]
        plaintext = ''.join(roundrobin(*plaintext_parts))
        print(f'{key_length}-key-long Vigenere cipher: \t{plaintext}')

    # Brute force
    for caesar_cipher_key in range(1, 26):
        print(
            f'Caesar cipher (key={caesar_cipher_key}): \t{caesar_cipher(ciphertext, caesar_cipher_key, reverse=True)}')


def question3():
    plaintext = 'UNALIENABLERIGHT'
    key = (3, 6, 2, 5)

    print(f'Plaintext:  {plaintext}')
    print(f'Ciphertext: {vigenere_cipher(plaintext, key)}')


def question4():
    plaintext = b'ATTACK'
    key = bytes((0b11010111, 0b11100101, 0b10001111, 0b00110000, 0b10100010, 0b00001010))
    cipher_text = one_time_pad(plaintext, key)

    print(f'Plaintext:  {BitString(plaintext)}')
    print(f'Key:        {BitString(key)}')

    print(f'Ciphertext: {BitString(int(cipher_text.hex(), base=16))}')
    print(f'Ciphertext: {HexString(cipher_text)}')


def question5():
    plaintext = BitString(0x0300004000700001.to_bytes(8, 'big'))
    key = '1000 0000 0000 0101 0000 0000 0001 0001 1000 0000 0000 1000'.replace(' ', '')
    key = BitString(key)

    binary_ciphertext = des_cipher(plaintext, key)
    hex_ciphertext = HexString(binary_ciphertext)

    print(f'Ciphertext: {binary_ciphertext}')
    print(f'Ciphertext: {hex_ciphertext}')


if __name__ == '__main__':
    logging.basicConfig(level=logging.DEBUG, stream=sys.stdout)

    print('Question 2: Frequency analysis')
    question2()

    print()
    print('Question 3: Vigenere cipher')
    question3()

    print()
    print('Question 4: One-time pad')
    question4()

    print()
    print('Question 5: DES')
    question5()
