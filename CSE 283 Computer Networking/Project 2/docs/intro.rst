Getting Started
===============

.. _Pipenv: https://docs.pipenv.org/

The preferred way of running this project is with Pipenv_::

    pipenv run python3 http_filter_proxy.py

Optionally, you can specify a port as a commandline argument::

    pipenv run python3 http_filter_proxy.py --port 8080

On Windows, you can omit ``python3``, in the case it is not on your PATH.

If you do not use Pipenv_, you'll need to manually install the following dependency
before you can launch this project::

    pip3 install urllib3 --user
    python3 http_filter_proxy.py

Features
========

* Synchronous HTTP proxying
* All HTTP methods supported
* Compatible with modern web browsers
* Supports HTTP/1.0 and HTTP/1.1
* Content censoring

   * URL-based
   * Content-based (when content is not compressed)

* Variable proxy port
* Supports automatic configuration (via a PAC file)

Unsupported Features
====================

* Concurrency
* Multiple sockets
  (only one connection to the proxy at a time is supported -
  this means only one can download at a time)
* Content decompression
* HTTP tunneling (HTTPS traffic not supported)
* Man-in-the-middle attacks in a TLS scheme
