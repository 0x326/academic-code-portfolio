[GitHub-Desktop]: https://desktop.github.com/
[EditorConfig-Download]: https://editorconfig.org/#download
[GitHub-Pull-Request]: https://github.com/0x326/miami-university-cse-464-group-project/compare

# CSE 464: Group Project

## Getting started

1. Download [GitHub Desktop][GitHub-Desktop]
1. Clone this repo using the current URL in your web browser
1. Open up the `miami-university-cse-464-group-project` folder with an IDE or text editor of your choice
1. Install the [EditorConfig][EditorConfig-Download] plugin for your IDE/text editor

### Git workflow

1. Decide on a feature you want to work on
1. Pull changes from GitHub
1. Create a new branch for that feature (ex: `add-some-feature-name`)
1. Work in your IDE/text editor
1. Add and commit your changes once you are done.
   Give a brief summary of what your changes are.
   Write in the imperative mood (ex: "Fix typo" instead of "Fixed typo")
1. Once your changes are good enough to share, push them to GitHub
1. If the feature is complete and bug-free,
   make a [pull request on GitHub][GitHub-Pull-Request] to merge your branch into the `master` branch
1. Then it can be reviewed by another group member and merged into `master`

You can also work on multiple features at once.
For this, you will need to maintain two branches and keep work for each feature separate.

While you are working, if you would like to switch to another branch
but you are not ready to make a commit,
you may want to look into `git stash`.
It effectively makes a temporary commit that you can resume later by running `git stash pop`.
If you have multiple stashes, you may want to reference them by name (see `git stash list`).
This feature can only be used from the commandline.

## License

### Code

MIT (see `LICENSE`)

### Non-code (documentation, etc.)

<a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text" rel="dct:type">work</span> by <a xmlns:cc="http://creativecommons.org/ns#" href="https://github.com/0x326/miami-university-cse-464-group-project" property="cc:attributionName" rel="cc:attributionURL">John Meyer, Scott Harris, Jacob Freedman, & Bryan Hayes</a> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
