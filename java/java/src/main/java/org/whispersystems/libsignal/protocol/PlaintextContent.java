/**
 * Copyright (C) 2014-2016 Open Whisper Systems
 *
 * Licensed according to the LICENSE file in this repository.
 */
package org.whispersystems.libsignal.protocol;

import org.signal.client.internal.Native;

import org.whispersystems.libsignal.InvalidMessageException;
import org.whispersystems.libsignal.util.guava.Optional;

public class PlaintextContent implements CiphertextMessage {

  private long handle;

  @Override
  protected void finalize() {
     Native.PlaintextContent_Destroy(this.handle);
  }

  public long nativeHandle() {
    return handle;
  }

  // Used by Rust.
  private PlaintextContent(long handle) {
    this.handle = handle;
  }

  public PlaintextContent(byte[] serialized) throws InvalidMessageException {
    handle = Native.PlaintextContent_Deserialize(serialized);
  }

  public PlaintextContent(DecryptionErrorMessage message) {
    handle = Native.PlaintextContent_FromDecryptionErrorMessage(message.handle);
  }

  @Override
  public byte[] serialize() {
    return Native.PlaintextContent_GetSerialized(this.handle);
  }

  @Override
  public int getType() {
    return CiphertextMessage.PLAINTEXT_CONTENT_TYPE;
  }

  public Optional<DecryptionErrorMessage> getDecryptionErrorMessage() {
    long messageHandle = Native.PlaintextContent_GetDecryptionErrorMessage(this.handle);
    if (messageHandle == 0) {
      return Optional.absent();
    } else {
      return Optional.of(new DecryptionErrorMessage(messageHandle));
    }
  }
}