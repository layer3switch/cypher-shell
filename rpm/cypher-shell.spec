Name: cypher-shell
Provides: cypher-shell
Version: 1.0.1
Release: 1%{?dist}
Summary: Command line shell for Neo4j

License: GPLv3
URL: https://github.com/neo4j/cypher-shell
Source0: https://github.com/neo4j/cypher-shell/archive/%{version}.tar.gz

Requires: java-1.8.0-headless
BuildArch: noarch
Prefix: /usr

%description
A command line shell where you can execute Cypher against an instance
of Neo4j.

%prep
# This macro will unpack the tarball into the appropriate build directory
%setup -q

%build
make clean build

%install
rm -rf ${RPM_BUILD_ROOT}
# Calls make with correct DESTDIR
%make_install prefix=/usr

%clean
rm -rf ${RPM_BUILD_ROOT}

%files
%defattr(-,root,root)
%{_bindir}/cypher-shell
%{_datadir}/cypher-shell
