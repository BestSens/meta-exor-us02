# Fix Mesa build
EXTRA_OEMAKE += " CFLAGS='${CFLAGS} -Wno-implicit-function-declaration -std=c11'"
