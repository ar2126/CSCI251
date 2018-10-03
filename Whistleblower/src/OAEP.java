import edu.rit.util.Packing;
import java.math.BigInteger;
import java.nio.charset.Charset;

/**
 * Class OAEP implements the Optimal Asymmetric Encryption Padding operations
 * for encoding strings for RSA encryption. OAEP is specified in Public Key
 * Cryptography Standard Number One (PKCS#1) version 2.2. A string is encoded as
 * a 2048-bit big integer.
 *
 * @author  Alan Kaminsky
 * @version 07-Apr-2018
 */
public class OAEP
	{

// Hidden data members.

	// UTF-8 character set.
	private static final Charset utf8 = Charset.forName ("UTF-8");

	// OAEP label hash.
	private byte[] lHash;

// Exported constructors.

	/**
	 * Construct a new OAEP string encoder.
	 */
	public OAEP()
		{
		lHash = new byte [32];
		sha256 (new byte [0], 0, 0, lHash, 0);
		}

// Exported operations.

	/**
	 * Encode the given string.
	 *
	 * @param  str   String.
	 * @param  seed  Random seed, a 32-byte array.
	 *
	 * @return  Encoded string, a big integer.
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if <TT>str</TT> is too long to be
	 *     encoded. Thrown if <TT>seed</TT> is not a 32-byte array.
	 */
	public BigInteger encode
		(String str,
		 byte[] seed)
		{
		// Convert string to byte array.
		byte[] m = str.getBytes (utf8);

		// Verify preconditions.
		if (m.length > 190)
			throw new IllegalArgumentException
				("OAEP.encode(): String is too long");
		if (seed.length != 32)
			throw new IllegalArgumentException
				("OAEP.encode(): seed must be 32 bytes");

		// Set up OAEP encoded output byte array.
		byte[] enc = new byte [256];

		// Store and pad the message.
		System.arraycopy (lHash, 0, enc, 33, 32);
		enc[255-m.length] = (byte) 0x01;
		System.arraycopy (m, 0, enc, 256 - m.length, m.length);

		// Mask the message using the seed.
		mask (enc, 33, 223, seed, 0, 32);

		// Store and mask the seed using the masked message.
		System.arraycopy (seed, 0, enc, 1, 32);
		mask (enc, 1, 32, enc, 33, 223);

		// Return encoded string.
		return new BigInteger (enc);
		}

	/**
	 * Decode the given big integer. The big integer must have been produced by
	 * the <TT>encode()</TT> method.
	 *
	 * @param  bigint  Encoded string.
	 *
	 * @return  Decoded string.
	 *
	 * @exception  IllegalArgumentException
	 *     (unchecked exception) Thrown if any error occurs during the decoding
	 *     process.
	 */
	public String decode
		(BigInteger bigint)
		{
		// Convert big integer to byte array.
		byte[] bi = bigint.toByteArray();
		if (bi.length > 256)
			throw new IllegalArgumentException
				("OAEP.decode(): bigint is too big");

		// Prepend 0 bytes as necessary.
		byte[] enc = new byte [256];
		System.arraycopy (bi, 0, enc, 256 - bi.length, bi.length);

		// Check that first byte is 0.
		if (enc[0] != 0x00)
			throw new IllegalArgumentException
				("OAEP.decode(): First byte incorrect");

		// Unmask the seed using the masked message.
		mask (enc, 1, 32, enc, 33, 223);

		// Unmask the message using the seed.
		mask (enc, 33, 223, enc, 1, 32);

		// Check that the first 32 bytes of padding equals lHash.
		for (int i = 0; i < 32; ++ i)
			if (enc[33+i] != lHash[i])
				throw new IllegalArgumentException
					("OAEP.decode(): lHash incorrect");

		// Scan over zero or more padding bytes of 0 until a padding byte of 1.
		int j = 65;
		while (j < 256 && enc[j] == 0x00) ++ j;
		if (j == 256 || enc[j] != 0x01)
			throw new IllegalArgumentException
				("OAEP.decode(): Padding incorrect");

		// Skip final padding byte; reconstruct original string.
		++ j;
		return new String (enc, j, 256 - j, utf8);
		}

// Hidden operations.

	/**
	 * Mask (or unmask) the given message using Mask Generating Function One
	 * (MGF1) with the given seed.
	 *
	 * @param  msgbuf   Message buffer (input/output).
	 * @param  msgoff   Index of first message byte to mask.
	 * @param  msglen   Number of message bytes to mask.
	 * @param  seedbuf  Seed buffer (input).
	 * @param  seedoff  Index of first seed byte.
	 * @param  seedlen  Number of seed bytes.
	 */
	private void mask
		(byte[] msgbuf,
		 int msgoff,
		 int msglen,
		 byte[] seedbuf,
		 int seedoff,
		 int seedlen)
		{
		// Verify preconditions.
		if (msgoff < 0 || msglen < 0 || msgoff + msglen > msgbuf.length ||
			seedoff < 0 || seedlen < 0 || seedoff + seedlen > seedbuf.length)
				throw new IllegalArgumentException();

		// Set up (seed || counter) SHA-256 input buffer.
		byte[] seedctrbuf = new byte [seedlen + 4];
		System.arraycopy (seedbuf, seedoff, seedctrbuf, 0, seedlen);

		// Initialize counter for input buffer.
		int ctr = 0;

		// Mask each byte of the message.
		for (int i = 0; i < msglen; ++ i)
			{
			// If necessary, generate next 32 mask bytes using SHA-256.
			if ((i & 31) == 0)
				{
				Packing.unpackIntBigEndian (ctr, seedctrbuf, seedlen);
				sha256 (seedctrbuf, 0, seedctrbuf.length, maskdig, 0);
				++ ctr;
				}
			msgbuf[msgoff+i] ^= maskdig[i & 31];
			}
		}

	// Digest byte array for mask() method.
	private byte[] maskdig = new byte [32];

	/**
	 * Compute the digest of the given message using the SHA-256 cryptographic
	 * hash function.
	 *
	 * @param  msgbuf  Message buffer (input).
	 * @param  msgoff  Index of first message byte to hash.
	 * @param  msglen  Number of message bytes to hash.
	 * @param  digbuf  Digest buffer (output).
	 * @param  digoff  Index of first digest byte to store. Indexes digoff
	 *                 through digoff+31 inclusive will be stored.
	 */
	private void sha256
		(byte[] msgbuf,
		 int msgoff,
		 int msglen,
		 byte[] digbuf,
		 int digoff)
		{
		// Verify preconditions.
		if (msgoff < 0 || msglen < 0 || msgoff + msglen > msgbuf.length ||
			digoff < 0 || digoff + 32 > digbuf.length)
				throw new IllegalArgumentException();

		// Determine message length in bits.
		long bitlen = 8L*msglen;

		// Set up padded message, consisting of message, one 0x80 padding byte,
		// zero or more 0x00 padding bytes, and 8-byte big-endian message length
		// (in bits), occuping a multiple of 64 bytes.
		int padlen = ((msglen + 9 + 63) >> 6) << 6;
		byte[] padbuf = new byte [padlen];
		System.arraycopy (msgbuf, msgoff, padbuf, 0, msglen);
		padbuf[msglen] = (byte) 0x80;
		Packing.unpackLongBigEndian (bitlen, padbuf, padlen - 8);

		// Initialize chaining value.
		H[0] = 0x6a09e667;
		H[1] = 0xbb67ae85;
		H[2] = 0x3c6ef372;
		H[3] = 0xa54ff53a;
		H[4] = 0x510e527f;
		H[5] = 0x9b05688c;
		H[6] = 0x1f83d9ab;
		H[7] = 0x5be0cd19;

		// Compress each 64-byte message block.
		for (int i = 0; i < padlen; i += 64)
			{
			// Prepare message schedule W.
			Packing.packIntBigEndian (padbuf, i, W, 0, 16);
			for (int t = 16; t <= 63; ++ t)
				W[t] = sigma_1(W[t-2]) + W[t-7] + sigma_0(W[t-15]) + W[t-16];

			// Initialize working variables.
			int a = H[0];
			int b = H[1];
			int c = H[2];
			int d = H[3];
			int e = H[4];
			int f = H[5];
			int g = H[6];
			int h = H[7];

			// Do 64 rounds.
			int T_1, T_2;
			for (int t = 0; t <= 63; ++ t)
				{
				T_1 = h + Sigma_1(e) + Ch(e,f,g) + K[t] + W[t];
				T_2 = Sigma_0(a) + Maj(a,b,c);
				h = g;
				g = f;
				f = e;
				e = d + T_1;
				d = c;
				c = b;
				b = a;
				a = T_1 + T_2;
				}

			// Output chaining value.
			H[0] += a;
			H[1] += b;
			H[2] += c;
			H[3] += d;
			H[4] += e;
			H[5] += f;
			H[6] += g;
			H[7] += h;
			}

		// Output digest.
		Packing.unpackIntBigEndian (H, 0, digbuf, digoff, 8);
		}

	// SHA-256 chaining value H.
	private int[] H = new int [8];

	// SHA-256 message schedule W.
	private int[] W = new int [64];

	// SHA-256 little functions.
	private static int Ch (int x, int y, int z)
		{
		return (x & y) ^ (~x & z);
		}
	private static int Maj (int x, int y, int z)
		{
		return (x & y) ^ (x & z) ^ (y & z);
		}
	private static int Sigma_0 (int x)
		{
		return Integer.rotateRight (x, 2) ^
			Integer.rotateRight (x, 13) ^
			Integer.rotateRight (x, 22);
		}
	private static int Sigma_1 (int x)
		{
		return Integer.rotateRight (x, 6) ^
			Integer.rotateRight (x, 11) ^
			Integer.rotateRight (x, 25);
		}
	private static int sigma_0 (int x)
		{
		return Integer.rotateRight (x, 7) ^
			Integer.rotateRight (x, 18) ^
			(x >>> 3);
		}
	private static int sigma_1 (int x)
		{
		return Integer.rotateRight (x, 17) ^
			Integer.rotateRight (x, 19) ^
			(x >>> 10);
		}

	// SHA-256 constants.
	private static int[] K = new int[]
		{
		0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 
		0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5, 
		0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 
		0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174, 
		0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 
		0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da, 
		0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 
		0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967, 
		0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 
		0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85, 
		0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 
		0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070, 
		0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 
		0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3, 
		0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 
		0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2, 
		};

	}
