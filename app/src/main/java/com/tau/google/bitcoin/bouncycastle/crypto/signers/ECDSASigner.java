package com.mofei.tau.google.bitcoin.bouncycastle.crypto.signers;

import com.mofei.tau.google.bitcoin.bouncycastle.crypto.CipherParameters;
import com.mofei.tau.google.bitcoin.bouncycastle.crypto.DSA;
import com.mofei.tau.google.bitcoin.bouncycastle.crypto.params.ECKeyParameters;
import com.mofei.tau.google.bitcoin.bouncycastle.crypto.params.ECPrivateKeyParameters;
import com.mofei.tau.google.bitcoin.bouncycastle.crypto.params.ECPublicKeyParameters;
import com.mofei.tau.google.bitcoin.bouncycastle.crypto.params.ParametersWithRandom;
import com.mofei.tau.google.bitcoin.bouncycastle.math.ec.ECAlgorithms;
import com.mofei.tau.google.bitcoin.bouncycastle.math.ec.ECConstants;
import com.mofei.tau.google.bitcoin.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * EC-DSA as described in X9.62
 */
public class ECDSASigner implements ECConstants, DSA {
    ECKeyParameters key;
    SecureRandom random;
    public void init(boolean forSigning,CipherParameters param) {
        if (forSigning) {
            if (param instanceof ParametersWithRandom) {
                ParametersWithRandom rParam = (ParametersWithRandom)param;
                this.random = rParam.getRandom();
                this.key = (ECPrivateKeyParameters)rParam.getParameters();
            } else {
                this.random = new SecureRandom();
                this.key = (ECPrivateKeyParameters)param;
            }
        } else {
            this.key = (ECPublicKeyParameters)param;
        }
    }

    // 5.3 pg 28
    /**
     * generate a signature for the given message using the key we were
     * initialised with. For conventional DSA the message should be a SHA-1
     * hash of the message of interest.
     *
     * @param message the message that will be verified later.
     */
    public BigInteger[] generateSignature(
        byte[] message)
    {
        BigInteger n = key.getParameters().getN();
        BigInteger e = calculateE(n, message);
        BigInteger r = null;
        BigInteger s = null;

        byte[] rBytes = null;
        byte[] sBytes = null;

        // 5.3.2
        do // generate s
        {
            BigInteger k = null;
            int        nBitLength = n.bitLength();

            do // generate r
            {
                do
                {
                    k = new BigInteger(nBitLength, random);
                }
                while (k.equals(ZERO));

                ECPoint p = key.getParameters().getG().multiply(k);

                // 5.3.3
                BigInteger x = p.getX().toBigInteger();

                r = x.mod(n);
                rBytes = r.toByteArray();
            }
            while (r.equals(ZERO) || (rBytes != null && rBytes.length != 32));

            BigInteger d = ((ECPrivateKeyParameters)key).getD();

            s = k.modInverse(n).multiply(e.add(d.multiply(r))).mod(n);
            sBytes = s.toByteArray();
        }
        while (s.equals(ZERO) || (sBytes != null && sBytes.length != 32));

        BigInteger[]  res = new BigInteger[2];

        res[0] = r;
        res[1] = s;

        return res;
    }

    // 5.4 pg 29
    /**
     * return true if the value r and s represent a DSA signature for
     * the passed in message (for standard DSA the message should be
     * a SHA-1 hash of the real message to be verified).
     */
    public boolean verifySignature(
        byte[]      message,
        BigInteger  r,
        BigInteger  s)
    {
        BigInteger n = key.getParameters().getN();
        BigInteger e = calculateE(n, message);

        // r in the range [1,n-1]
        if (r.compareTo(ONE) < 0 || r.compareTo(n) >= 0)
        {
            return false;
        }

        // s in the range [1,n-1]
        if (s.compareTo(ONE) < 0 || s.compareTo(n) >= 0)
        {
            return false;
        }

        BigInteger c = s.modInverse(n);

        BigInteger u1 = e.multiply(c).mod(n);
        BigInteger u2 = r.multiply(c).mod(n);

        ECPoint G = key.getParameters().getG();
        ECPoint Q = ((ECPublicKeyParameters)key).getQ();

        ECPoint point = ECAlgorithms.sumOfTwoMultiplies(G, u1, Q, u2);

        BigInteger v = point.getX().toBigInteger().mod(n);

        return v.equals(r);
    }

    private BigInteger calculateE(BigInteger n, byte[] message)
    {  
        if (n.bitLength() > message.length * 8)
        {
            return new BigInteger(1, message);
        }
        else
        {
            int messageBitLength = message.length * 8;
            BigInteger trunc = new BigInteger(1, message);

            if (messageBitLength - n.bitLength() > 0)
            {
                trunc = trunc.shiftRight(messageBitLength - n.bitLength());
            }

            return trunc;
        }
    }
}
