[Xdebug-helper-chrome]: https://github.com/mac-cain13/xdebug-helper-for-chrome
[Xdebug-helper-firefox]: https://github.com/BrianGilbert/xdebug-helper-for-firefox

# Server

## Getting started

```shell
sudo make apt-install
make install
make serve
```

## Debugging in PhpStorm

1. Start local PHP server
1. Install Xdebug helper web browser extension ([Chrome][Xdebug-helper-chrome], [Firefox][Xdebug-helper-firefox])
1. Click "Start Listening for PHP Debug Connections" in PhpStorm
1. Set a breakpoint in PhpStorm
1. Open the PHP page in the web browser
1. Click "Debug" in Xdebug browser extension
1. Refresh page
