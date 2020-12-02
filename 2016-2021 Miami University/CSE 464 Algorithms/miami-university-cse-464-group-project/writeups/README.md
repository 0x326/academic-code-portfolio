[Wikibook-LaTeX]: https://en.wikibooks.org/wiki/LaTeX
[Scoop]: https://scoop.sh/
[Chocolatey]: https://chocolatey.org/
[Homebrew]: https://brew.sh/

# Project Writeups

Uses [LaTeX][Wikibook-LaTeX]

## Prerequisites

- LaTeX

  - Windows ([Scoop]): `scoop install latex`
  - Windows ([Chocolatey]): `choco install miktex`
  - macOS ([Homebrew]): `brew tap caskroom/cask && brew cask install mactex`
  - Debian: `apt install latexmk texlive-science`

- Perl

  - Windows ([Scoop]): `scoop install perl`
  - Windows ([Chocolatey]): `choco install strawberryperl`
  - macOS ([Homebrew]): `brew install perl`

- GNU make

  - Windows ([Scoop]): `scoop install make`
  - Windows ([Chocolatey]): `choco install make`
  - macOS ([Homebrew]): `brew install make`

## Building

Just run:

    cd ..
    make report
