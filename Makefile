all: buildNoExtension

buildNoExtension:
	@echo "Building timecard..."
	./gradlew linkReleaseExecutableNative

clean:
	@echo "Cleaning up..."
	rm -rf build

install:
	@echo "Installing timecard..."
	sudo cp ./build/bin/native/releaseExecutable/timecard.kexe /usr/local/bin/timecard
	@echo "timecard has been installed to /usr/local/bin. Use 'timecard help' for a list of commands."
