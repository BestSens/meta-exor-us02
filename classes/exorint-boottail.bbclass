# Add bootldrTail header at end of bootloader files [SHW299.docx].
#
# typedef struct _bootldrTail
# {
#     unsigned char reserved;
#     unsigned char day;
#     unsigned char month;
#     unsigned char year;
#     unsigned char version[BLDRVERSIONSIZE];
#     unsigned int fileChecksum:32;
#     unsigned int headerChecksum:32;
# } bootldrTail;

python do_boot_tail () {

    import binascii
    import ctypes
    import shutil
    import struct
    from datetime import date

    srcdir = d.getVar('S', True)
    destdir = d.getVar('D', True)
    deploydir = d.getVar('DEPLOYDIR', True)
    splbinary = d.getVar('SPL_BINARY', True)
    ubootbinary = d.getVar('UBOOT_BINARY', True)
    splonly = d.getVar('SPL_ONLY', True)
    ubootonly = d.getVar('UBOOT_ONLY', True)
    version = d.getVar('VERSION', True)

    today = date.today()

    bb.note("Calculating boot tail given V: %s" % (version))

    # format all bootldrTail header except headerChecksum
    s = struct.pack('cccc20sI',
            chr(0),
            chr(today.day),
            chr(today.month),
            chr(today.year-2020),
            version,
            0
        )

    if (len(s) % 4) != 0:
        bb.fatal("Struct size must be a multiple of 4 (for checksum)!")

    # calculate checksum
    chksum = 0
    for i in range(0,len(s)/4):
        chksum += struct.unpack('I', s[i*4:i*4+4])[0]

    # append checksum to header and it's ready
    s += struct.pack('I', chksum)

    hexs = binascii.hexlify(s)
    bb.note("tail (hex): %s" % hexs)

    targetdirs = []
    targetdirs.append(srcdir)

    bb.note("targetdirs: " + str(targetdirs))

    for dir in targetdirs:

        if splonly == "1":
            loader = os.path.join(dir, splbinary)
            bb.note("SPL only - loader: " + loader)
        elif ubootonly == "1":
            loader = os.path.join(dir, ubootbinary)
            bb.note("UBOOT only - loader: " + loader)
        else:
            bb.fatal("Either SPL_ONLY or UBOOT_ONLY must be set!")

        if not os.path.exists(loader):
            bb.note("File not found!")
            continue

        if os.system('grep %s %s' % (version, loader)) == 0:
            bb.note("Tail already exists, not appending!")
            continue

        # DEBUG: keep a copy of the original file
        shutil.copy(loader, loader + '.orig')

        loaderfile = open(loader, 'a')

        try:
            loaderfile.write(s)
        finally:
            loaderfile.close()
}
