all: build

build:
	@echo "Building timecard..."
	./gradlew linkReleaseExecutableNative

clean:
	@echo "Cleaning up..."
	rm -rf ./build

install:
	@echo "Installing timecard..."
	cp ./build/bin/native/releaseExecutable/timecard.kexe ${prefix}/timecard
	@echo "timecard has been installed to ${prefix}. Use 'timecard help' for a list of commands."
