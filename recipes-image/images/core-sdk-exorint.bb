DESCRIPTION = "Exor International SDK"

require meta-toolchain-qt.inc

inherit populate_sdk

TOOLCHAIN_TARGET_TASK_append = " libmodbus-dev"

TOOLCHAIN_OUTPUTNAME = "exor-alterakit-sdk"
