# Story Teller Program - Documentation

## What you need to know:

When you see [square brackets], enter a number greater than zero.

## Introduction:

This program creates a simple story.  The story varies depending on the input.  All input (except for the name) must be a number in the counting number range (1, 2, 3 …).  The program will run the input through arbitrary formulas and will choose an element to add to the story.

## Run through:

The program will first ask for the character's name and gender.  The name can be in words.  The gender must be entered as 'M' or 'F'.  For a female adventurer: enter 'F'.  For a male adventurer: enter 'M'.

The program will output: "Once upon a time, there was an adventurer named (character name) and was [ number ] years old," with the character's name inserted.

Upon seeing square brackets, know to enter a counting number.

The program will out: "(He) went on a quest to (the Shire – Narnia – the Caribbean)" – first replacing "He" with the appropriate pronoun then attaching the location based on the previous input.  The program will evaluate the following expression: $(characterAge * 70) \mod 3$.  The character's age is multiplied by 70 then divided by 3.  The remainder of the division will be used to calculate the location.  If the result is 0, the location will be the Shire.  If it is 1: Narnia.  If it is 2: the Caribbean.

The program will continue to output: "At the early hour of [ number ]."  The program will wait for input then will output "(He) had an encounter with (dwarves, fauns, pirates) of which there were [ number ]."  The program will replace the type of guests based on the following equation: ${ hourOfDay }^5  \mod 3$.  If the result is 0, the type will be dwarves.  If it is 1: Fauns.  If it is 2: Pirates.  The program will again wait for input.  **EXCEPTION: This input must be greater than one.**  After waiting, it will output: "They decided to (eat all the food – kidnap (him) – steal all (his) goods)" and replace 'him' and 'his' with the appropriate part of speech.   The action of the guests will be calculated by the following formula: ${ numberOfGuests } ^3 \mod 3$.  If the result is 0: the guests will eat the food.  If 1: kidnap.  If 2: Steal goods.

The program will output: "This happened over the course of [ number ] days."  The program will wait for input.  The program will output: "but (he) eventually (moved away – was reimbursed – came to an agreement with them) and all was well" while replacing 'he' with the proper pronoun.  His action based on the expression: ${ daysOfGuestActivity }^7 \mod 3$.  If the result is 0: he will move away.  If it is 1: he will be reimbursed.  If it is 2: he will come to an agreement.  The program will finish by reprinting the story with the input replaced along with "the end."
