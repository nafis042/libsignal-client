/**
 * Copyright (C) 2014-2016 Open Whisper Systems
 *
 * Licensed according to the LICENSE file in this repository.
 */
package org.whispersystems.libsignal.protocol;

import org.signal.client.internal.Native;

import org.whispersystems.libsignal.InvalidMessageException;
import org.whispersystems.libsignal.ecc.ECPublicKey;
import org.whispersystems.libsignal.util.guava.Optional;

public class DecryptionErrorMessage {

  long handle;

  @Override
  protected void finalize() {
     Native.DecryptionErrorMessage_Destroy(this.handle);
  }

  public long nativeHandle() {
    return handle;
  }

  DecryptionErrorMessage(long handle) {
    this.handle = handle;
  }

  public DecryptionErrorMessage(byte[] serialized) throws InvalidMessageException {
    handle = Native.DecryptionErrorMessage_Deserialize(serialized);
  }

  public static DecryptionErrorMessage forOriginalMessage(byte[] originalBytes, int messageType, long timestamp) {
    return new DecryptionErrorMessage(
      Native.DecryptionErrorMessage_ForOriginalMessage(originalBytes, messageType, timestamp));
  }

  public byte[] serialize() {
    return Native.DecryptionErrorMessage_GetSerialized(this.handle);
  }

  public Optional<ECPublicKey> getRatchetKey() {
    long keyHandle = Native.DecryptionErrorMessage_GetRatchetKey(this.handle);
    if (keyHandle == 0) {
      return Optional.absent();
    } else {
      return Optional.of(new ECPublicKey(keyHandle));
    }
  }

  public long getTimestamp() {
    return Native.DecryptionErrorMessage_GetTimestamp(this.handle);
  }
}