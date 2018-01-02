HTTP Filter Proxy Documentation
===============================

   Requires Python 3.6

This project is a simple alternative to commercial internet censors.
It can provide content and URL-based censorship of sites sent over HTTP.
Note: modern web sites are migrating to HSTS, an all-HTTPS policy,
which defeats all censorship a proxy can provide due to the guarantee's of public-key cryptography.
See `Wikipedia <https://en.wikipedia.org/wiki/HTTPS>`_ for more info.

This is a synchronous program.
As such, it will download the entirety of a requested file before downloading the next one.
So, for example, if the web browser requests a 500MB file then a 5kB file,
the proxy will forward all 500MB before requesting the 5kB.
This can lead to unnecessary blocking in real-time applications.

.. toctree::
   user-manual
   code
   :maxdepth: 2
   :caption: Contents:

Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`
