/*
 * Copyright (c) 2011-2014 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.core.net;

import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.net.impl.KeyStoreHelper;

import javax.net.ssl.TrustManagerFactory;
import java.util.function.Function;

/**
 * Certification authority configuration options.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface TrustOptions {

  /**
   * @return a copy of these options
   */
  TrustOptions clone();

  /**
   * Create and return the trust manager factory for these options.
   * <p>
   * The returned trust manager factory should be already initialized and ready to use.
   *
   * @param vertx the vertx instance
   * @return the trust manager factory
   */
  default TrustManagerFactory getTrustManagerFactory(Vertx vertx) throws Exception {
    return KeyStoreHelper.create((VertxInternal) vertx, this).getTrustMgrFactory((VertxInternal) vertx);
  }

  /**
   * Returns a function that maps SNI server names to a {@link TrustManagerFactory} instance.
   *
   * The returned {@code TrustManagerFactory} must already be initialized and ready to use.
   *
   * The mapper is only used when the server has SNI enabled and the client indicated a server name.
   * <p/>
   * The returned function may return null in which case {@link #getTrustManagerFactory(Vertx)} is used as fallback.
   *
   * @param vertx the vertx instance
   * @return the trustManager
   */
  default Function<String, TrustManagerFactory> trustManagerMapper(Vertx vertx) throws Exception {
    KeyStoreHelper helper = KeyStoreHelper.create((VertxInternal) vertx, this);
    if (helper == null){
      // if there is no KeyStoreHelper for the concrete TrustOptions type return a function which always returns null.
      return (hostName) -> null;
    }
    return helper::getTrustMgr;
  }
}
