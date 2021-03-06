# CSE 271: Digital Appointment Book

<video src="https://cdn.rawgit.com/0x326/academic-code-portfolio/6b436935/CSE%20271/Project%201/documentation_assets/demoRunOfAppointmentBookTester.webm" controls></video>

## Specifications

- Design the following classes as described.
- `AppointmentBook` stores a collection of `Appointment` objects.
  - It has a method to add appointments; Accepts `enum appointmentType`, `String description`, and `String/GregorianCalendar date`.
  - It has a method to save an `Appointment` to a file.
  - It has a method to read an `Appointment` from a file.
- `Appointment` is an abstract superclass to:
  - `Onetime`
  - `Daily`
  - `Monthly`
- Every `Appointment` has a textual description and a `GregorianCalendar` date.
- `Appointment` has the method `abstract boolean occursOn(int year, int month, int day)`.
- Provide a tester class where the user enters a date and sees which events occur on that date.

## Features

- Uses a complex set of regular expressions to accurately parse a JSON file storing various `Appointment`'s, even when an `Appointment`'s textual description contains escaped characters and special delimiters, such as commas and curly braces.

## `Appointment` File Format: JSON

For this project, I chose to read and save `Appointment` objects in the JSON file format.  `Appointment`'s are stored in the following fashion:

```JSON
[
  {
    "_objectType": "Daily",
    "textualDescription": "Homework",
    "date": {
      "_objectType": "GregorianCalendar",
      "year": "2017",
      "month": "0",
      "day": "25"
    }
  },
  {
    "_objectType": "Monthly",
    "textualDescription": "Monthly Quiz",
    "date": {
      "_objectType": "GregorianCalendar",
      "year": "2017",
      "month": "0",
      "day": "30"
    }
  }
]
```
