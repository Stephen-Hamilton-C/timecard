package config

enum class BooleanOptions(val value: Boolean) {
	TRUE(true),
	FALSE(false);
	
	companion object {
		fun from(value: Boolean): BooleanOptions {
			return if (value) {
				TRUE
			} else {
				FALSE
			}
		}
	}
}
