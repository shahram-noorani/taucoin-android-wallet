package com.mofei.tau.google.bitcoin.bouncycastle.asn1;

import java.io.InputStream;

public interface ASN1OctetStringParser
    extends DEREncodable
{
    public InputStream getOctetStream();
}
