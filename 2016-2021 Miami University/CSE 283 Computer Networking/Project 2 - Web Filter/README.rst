.. _Pipenv: https://docs.pipenv.org/
.. _Sphinx: http://www.sphinx-doc.org/en/stable/

Web Filter
==========

Requires Python 3.6

Running
-------

::

   pip3 install pipenv
   pipenv install
   pipenv run python http_filter_proxy.py

Documentation
-------------

Written with Sphinx_.
View the documentation at ``docs/_build/html/index.html``
by building it with::

   pipenv install --dev
   cd docs/
   pipenv run make html
   # Open _build\html
