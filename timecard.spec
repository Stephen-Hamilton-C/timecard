Name:           timecard
Version:        0
Release:        0
Summary:        Simple commandline time tracking program, written in Kotlin Native
License:        GPLv3
URL:            https://github.com/Stephen-Hamilton-C/timecard
Source0:        https://github.com/Stephen-Hamilton-C/timecard/archive/refs/tags/v%{version}.tar.gz

BuildRequires:  java-devel

%description
timecard is a command line time tracking program
written by a developer, for developers.
Quick syntax allows time tracking to be easy, while also being customizable.
Supports quarter-hour rounding, 24-hour time, and automatic cleanup.
Written in Kotlin Native, for rapid fast response.

%global debug_package %{nil}

%prep
%setup -q

%build
./gradlew linkReleaseExecutableNative

%install
mkdir -p %{buildroot}/usr/bin/
install -m 755 ./build/bin/native/releaseExecutable/timecard.kexe %{buildroot}/usr/bin/timecard

%check
./gradlew nativeTest

%files
/usr/bin/timecard
