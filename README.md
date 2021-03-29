# PostgreSQL

## What is PostgreSQL

Let’s start with a simple question: what is PostgreSQL?

PostgreSQL is an advanced, enterprise-class, and open-source relational database system. PostgreSQL supports both SQL (relational) and JSON (non-relational) querying.

PostgreSQL is a highly stable database backed by more than 20 years of development by the open-source community.

### History of PostgreSQL

The PostgreSQL project started in 1986 at Berkeley Computer Science Department, University of California.

The project was originally named POSTGRES, in reference to the older Ingres database which also developed at Berkeley. The goal of the POSTGRES project was to add the minimal features needed to support multiple data types.

In 1996, the POSTGRES project was renamed to PostgreSQL to clearly illustrate its support for SQL. Today, PostgreSQL is commonly abbreviated as Postgres.

Since then, the PostgreSQL Global Development Group, a dedicated community of contributors continues to make the releases of the open-source and free database project.

Originally, PostgreSQL was designed to run on UNIX-like platforms. And then, PostgreSQL was evolved run on various platforms such as Windows, macOS, and Solaris.

### Common Use cases of PostgreSQL
1) ***A robust database in the LAPP stack***:
LAPP stands for Linux, Apache, PostgreSQL, and PHP (or Python and Perl). PostgreSQL is primarily used as a robust back-end database that powers many dynamic websites and web applications.

2) ***General purpose transaction database***:
Large corporations and startups alike use PostgreSQL as primary databases to support their applications and products.

3) ***Geospatial database***:
PostgreSQL with the PostGIS extension supports geospatial databases for geographic information systems (GIS).

### Language support

PostgreSQL support most popular programming languages:

- Python
- Java
- C#
- C/C+
- Ruby
- JavaScript (Node.js)
- Perl
- Go
- Tcl

### PostgreSQL feature highlights

PostgreSQL has many advanced features that other enterprise-class database management systems offer, such as:

- User-defined types
- Table inheritance
- Sophisticated locking mechanism
- Foreign key referential integrity
- Views, rules, subquery
- Nested transactions (savepoints)
- Multi-version concurrency control (MVCC)
- Asynchronous replication

The recent versions of PostgreSQL support the following features:

- Native Microsoft Windows Server version
- Tablespaces
- Point-in-time recovery

PostgreSQL is designed to be extensible. PostgreSQL allows you to define your own data types, index types, functional languages, etc.

### Who uses PostgreSQL

Many companies have built products and solutions based on PostgreSQL. Some featured companies are Apple, Fujitsu, Red Hat, Cisco, Juniper Network, Instagram, etc.


## Install PostgreSQL on Windows

There are 4 software components to install:
- The PostgreSQL Server to install the PostgreSQL database server
- pgAdmin 4 to install the PostgreSQL database GUI management tool.
- Command Line Tools to install command-line tools such as psql, pg_restore, etc. These tools allow you to interact with the PostgreSQL database server using the command-line interface.
- Stack Builder provides a GUI that allows you to download and install drivers that work with PostgreSQL.

### Verify the Installation

There are several ways to verify the PostgreSQL installation. You can try to connect to the PostgreSQL database server from any client application e.g.,  psql and pgAdmin.

The quick way to verify the installation is through the psql program.

First, click the psql application to launch it. The psql command-line program will display.

![SQL Shell (psql)](https://sp.postgresqltutorial.com/wp-content/uploads/2020/07/Install-PostgreSQL-psql.png)

Second, enter all the necessary information such as the server, database, port, username, and password. To accept the default, you can press **Enter**.  Note that you should provide the password that you entered during installing the PostgreSQL.

```
Server [localhost]:
Database [postgres]:
Port [5432]:
Username [postgres]:
Password for user postgres:
psql (12.3)
WARNING: Console code page (437) differs from Windows code page (1252)
         8-bit characters might not work correctly. See psql reference
         page "Notes for Windows users" for details.
Type "help" for help.

postgres=#
```

Third, issue the command SELECT version(); you will see the following output:

```
SELECT version();
```

Congratulation! you’ve successfully installed PostgreSQL database server on your local system. Let’s learn various ways to connect to PostgreSQL database server.

---

References:

[PostgreSQL Tutorial](https://www.postgresqltutorial.com/install-postgresql/)
[PostgreSQL - Copy Database](https://www.postgresqltutorial.com/postgresql-copy-database/)