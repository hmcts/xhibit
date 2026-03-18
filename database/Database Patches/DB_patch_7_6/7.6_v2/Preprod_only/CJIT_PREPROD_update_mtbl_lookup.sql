update mtbl_lookup set lookup1 = 'http://130.177.1.103/cjipwebservice/cjipwebservice.asmx', lookup2 = 'HTTP://csg00100.loccs.gov.uk:8080',lookup3 = '',lookup4 = '', lookup5 = 'mercator', lookup6 = 'password', lookup7 =  '-TUNNELING -I 500 -TIMEOUT 20', lookup8 = '' where lookup_type = 'HTTP_SERVER' and lookup_code = 'CJIP_EVENT';

update mtbl_lookup set lookup1 = 'http://prodgw.xmlgw.mobilesys.net/httpg/send', lookup2 = 'HTTP://10.31.10.36:8080',lookup3 = '',lookup4 = '', lookup5 = 'mercator', lookup6 = 'mercator', lookup7 =  '-TUNNELING -I 500', lookup8 = '' where lookup_type = 'HTTP_SERVER' and lookup_code = 'CJIT_SEND';

update mtbl_lookup set lookup1 = 'http://130.177.1.103/cjipwebservice/cjipwebservice.asmx', lookup2 = 'HTTP://csg00100.loccs.gov.uk:8080',lookup3 = '',lookup4 = '', lookup5 = 'mercator', lookup6 = 'password', lookup7 =  '-TUNNELING -I 500', lookup8 = '' where lookup_type = 'HTTP_SERVER' and lookup_code = 'POLL_CJIP';

update mtbl_lookup set lookup1 = 'http://prodgw.xmlgw.mobilesys.net/httpg/status', lookup2 = 'HTTP://10.31.10.36:8080',lookup3 = '',lookup4 = '', lookup5 = 'mercator', lookup6 = 'mercator', lookup7 =  '-TUNNELING -I 500', lookup8 = '' where lookup_type = 'HTTP_SERVER' and lookup_code = 'CJIT_STATUS';

update mtbl_lookup set lookup1 = 'HTTPS://cjip001.cjsonline.gsi.gov.uk/bitswebservice/bitswebservice.asmx', lookup2 = 'HTTP://csg00100.loccs.gov.uk:8080',lookup3 = '3DSpr3pr0d',lookup4 = 's.pem', lookup5 = 'mercator', lookup6 = 'password', lookup7 =  '-TUNNELING -I 500', lookup8 = '-CERT -PKEY -CA -KPASS' where lookup_type = 'HTTP_SERVER' and lookup_code = 'BITS_DOC_REG';

update mtbl_lookup set lookup1 = 'edsux3:1:http1', lookup2 = 'p9s72',lookup3 = '',lookup4 = '', lookup5 = '', lookup6 = '', lookup7 =  '', lookup8 = '' where lookup_type = 'USER_PASS' and lookup_code = 'CJIT';

update mtbl_lookup set lookup1 = 'http://prodgw.xmlgw.mobilesys.net/httpg/inbound', lookup2 = 'HTTP://10.31.10.36:8080',lookup3 = '',lookup4 = '', lookup5 = 'mercator', lookup6 = 'mercator', lookup7 =  '-TUNNELING -I 500', lookup8 = '' where lookup_type = 'HTTP_SERVER' and lookup_code = 'CJIT_INBOUND';

update mtbl_lookup set lookup1 = 'http://130.177.1.103/cjipwebservice/cjipwebservice.asmx', lookup2 = 'HTTP://csg00100.loccs.gov.uk:8080',lookup3 = '',lookup4 = '', lookup5 = 'mercator', lookup6 = 'password', lookup7 =  '-TUNNELING -I 500 -TIMEOUT 20', lookup8 = '' where lookup_type = 'HTTP_SERVER' and lookup_code = 'CJIP_DOC_DEREG';

update mtbl_lookup set lookup1 = 'HTTPS://cjip001.cjsonline.gsi.gov.uk/bitswebservice/bitswebservice.asmx', lookup2 = 'HTTP://csg00100.loccs.gov.uk:8080',lookup3 = '3DSpr3pr0d',lookup4 = 's.pem', lookup5 = 'mercator', lookup6 = 'password', lookup7 =  '-TUNNELING -I 500', lookup8 = '-CERT -PKEY -CA -KPASS' where lookup_type = 'HTTP_SERVER' and lookup_code = 'BITS_DOC_SUBMIT';

update mtbl_lookup set lookup1 = 'HTTPS://cjip001.cjsonline.gsi.gov.uk/bitswebservice/bitswebservice.asmx', lookup2 = 'HTTP://csg00100.loccs.gov.uk:8080',lookup3 = '3DSpr3pr0d',lookup4 = 's.pem', lookup5 = 'mercator', lookup6 = 'password', lookup7 =  '-TUNNELING -I 500', lookup8 = '-CERT -PKEY -CA -KPASS' where lookup_type = 'HTTP_SERVER' and lookup_code = 'POLL_BITS';
