[Vagrant]: https://www.vagrantup.com/

# Database

## Login info

- Host: `127.0.0.1`
- Username: `root`
- Password: `root`

## Getting started

- Install [Vagrant][Vagrant]
- Start database VM

```bash
cd mysql-vagrant/
vagrant up
```

- Initialize database with DDL

```bash
mysql -h 127.0.0.1 -u root -p < DDL.sql
```
