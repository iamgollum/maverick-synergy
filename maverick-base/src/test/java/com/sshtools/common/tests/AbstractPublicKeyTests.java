/**
 * (c) 2002-2021 JADAPTIVE Limited. All Rights Reserved.
 *
 * This file is part of the Maverick Synergy Java SSH API.
 *
 * Maverick Synergy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Maverick Synergy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Maverick Synergy.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.sshtools.common.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.junit.Ignore;

import com.sshtools.common.publickey.InvalidPassphraseException;
import com.sshtools.common.publickey.SshKeyPairGenerator;
import com.sshtools.common.publickey.SshPrivateKeyFile;
import com.sshtools.common.publickey.SshPrivateKeyFileFactory;
import com.sshtools.common.publickey.SshPublicKeyFile;
import com.sshtools.common.publickey.SshPublicKeyFileFactory;
import com.sshtools.common.ssh.SshException;
import com.sshtools.common.ssh.components.SshKeyPair;
import com.sshtools.common.ssh.components.SshPublicKey;
import com.sshtools.common.util.ByteArrayWriter;
import com.sshtools.common.util.IOUtils;

import junit.framework.TestCase;

@Ignore
public abstract class AbstractPublicKeyTests extends TestCase {

	protected abstract String getTestingJCE();
	
	protected void testPrivateKeyFile(InputStream in, String passphrase, String fingerprint) throws IOException, InvalidPassphraseException, SshException {
		try {
			SshPrivateKeyFile file = SshPrivateKeyFileFactory.parse(in);
			SshKeyPair pair = file.toKeyPair(passphrase);
			String fingerprint2 = pair.getPublicKey().getFingerprint();
			assertEquals(fingerprint, fingerprint2);
			assertEquals(getTestingJCE(), pair.getPublicKey().test());

		} finally {
			IOUtils.closeStream(in);
		}
	}
	
	protected void testPrivateKeyFile(InputStream in, String passphrase, String fingerprint, String jce) throws IOException, InvalidPassphraseException, SshException {
		try {
			SshPrivateKeyFile file = SshPrivateKeyFileFactory.parse(in);
			SshKeyPair pair = file.toKeyPair(passphrase);
			String fingerprint2 = pair.getPublicKey().getFingerprint();
			assertEquals(fingerprint, fingerprint2);
			assertEquals(jce, pair.getPublicKey().test());

		} finally {
			IOUtils.closeStream(in);
		}
	}
	
	protected void testPublicKeyFile(InputStream in, String fingerprint) throws IOException, InvalidPassphraseException, SshException {
		try {
			SshPublicKeyFile file = SshPublicKeyFileFactory.parse(in);
			SshPublicKey key = file.toPublicKey();
			String fingerprint2 = key.getFingerprint();
			assertEquals(fingerprint, fingerprint2);
			assertEquals(getTestingJCE(), key.test());
		} finally {
			IOUtils.closeStream(in);
		}
	}
	
	protected SshKeyPair testKeyGeneration(String type, int bits, String passphrase, String comment, int format) throws IOException, SshException, InvalidPassphraseException {
		
		SshKeyPair generated = testKeyGeneration(type, bits);
		assertEquals(generated.getPublicKey().getBitLength(), bits);
		assertEquals(getTestingJCE(), generated.getPublicKey().test());
		byte[] formattedkey = saveKeyPair(generated, passphrase, format);
		SshKeyPair loaded = loadKeyPair(formattedkey, passphrase);
		assertEquals(getTestingJCE(), loaded.getPublicKey().test());
		checkKeyPairs(generated, loaded);
		return generated;
	}
	
	protected SshKeyPair testKeyGeneration(String type, int bits) throws IOException, SshException {
		return SshKeyPairGenerator.generateKeyPair(type, bits);
	}
	
	protected byte[] saveKeyPair(SshKeyPair pair, String passphrase, int format) throws IOException {
		SshPrivateKeyFile file = SshPrivateKeyFileFactory.create(pair, passphrase, format);
		return file.getFormattedKey();
	}
	
	protected SshKeyPair loadKeyPair(byte[] formattedkey, String passphrase) throws IOException, InvalidPassphraseException {
		SshPrivateKeyFile file = SshPrivateKeyFileFactory.parse(formattedkey);
		return file.toKeyPair(passphrase);
	}
	
	protected SshKeyPair loadKeyPair(InputStream in, String passphrase) throws IOException, InvalidPassphraseException {
		try {
			SshPrivateKeyFile file = SshPrivateKeyFileFactory.parse(in);
			return file.toKeyPair(passphrase);
		} finally {
			IOUtils.closeStream(in);
		}
	}
	
	protected void checkKeyPairs(SshKeyPair first, SshKeyPair second) {
		assertTrue("Key pairs must be equal", first.equals(second));
	}
	
	protected void testSignatures(SshKeyPair pair, int iterations) throws IOException, SshException {
		
		byte[] tmp = new byte[16384];
		Random rnd = new Random();
		for(int i=0;i<iterations;i++) {
			rnd.nextBytes(tmp);
			byte[] sig = pair.getPrivateKey().sign(tmp, pair.getPublicKey().getSigningAlgorithm());
			ByteArrayWriter baw = new ByteArrayWriter();
			try {
				baw.writeString(pair.getPublicKey().getSigningAlgorithm());
	            baw.writeBinaryString(sig);
				assertTrue("Invalid signature", pair.getPublicKey().verifySignature(baw.toByteArray(), tmp));
			} finally {
				baw.close();
			}
		}
	}
}
