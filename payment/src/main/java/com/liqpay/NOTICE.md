# Vendored LiqPay Java SDK

Source: https://github.com/liqpay/sdk-java (branch `master`, package `com.liqpay`)
License: Apache License 2.0 — http://www.apache.org/licenses/LICENSE-2.0.txt

## Why this is vendored rather than resolved from a repository

The build used to pull `com.liqpay:liqpay-sdk:0.7-SNAPSHOT` from
`https://github.com/liqpay/sdk-java/raw/repository`, a Maven repository served
out of a git branch. That artifact no longer exists — the branch now carries
only `0.8-SNAPSHOT`, and it publishes `maven-metadata-local.xml` instead of the
`maven-metadata.xml` a Maven repository is supposed to expose. The result was a
hard build failure:

    Could not find com.liqpay:liqpay-sdk:0.7-SNAPSHOT.

A SNAPSHOT served from a mutable git branch is not a reproducible dependency in
the first place, so the four source files are copied here instead. The package
name is kept as `com.liqpay` so call sites are unchanged.

## Deliberate differences from upstream

1. **JSON handling uses Jackson instead of `json-simple`.** `json-simple` 1.1.1
   was last released in 2012 and leaks junit onto the compile classpath.
   Jackson is already a dependency of this service. `LiqPayUtil.getArray` and
   `LiqPayUtil.parseJson`, which only existed to walk `json-simple` types, are
   gone; `LiqPay.api` deserialises straight into a `Map`.

2. **`javax.xml.bind.DatatypeConverter` replaced with `java.util.Base64`.**
   JDK 11 removed `java.xml.bind` from the JDK, and the `payment` module never
   declared the JAXB shim that `discovery` and `proxy` carry. Any call through
   `LiqPayUtil.base64_encode` would have died with `NoClassDefFoundError` at
   runtime. Both produce identical unpadded-free standard Base64.

3. **`base64_encode(String)` now encodes as UTF-8.** Upstream calls
   `data.getBytes()`, which uses the platform default charset. The service
   containers run with a POSIX/ASCII default locale, so any Cyrillic
   `description` was mangled to `?` before being signed — meaning `data` and
   `signature` were both computed over corrupted bytes. LiqPay's API specifies
   UTF-8.

4. **`LiqPayRequest.post` sets connect and read timeouts.** Upstream leaves the
   `HttpURLConnection` with no timeouts at all, so a stalled LiqPay endpoint
   would pin a request thread indefinitely.

Points 3 and 4 change observable behaviour. They are corrections, but anyone
re-enabling this service should verify a live checkout against a LiqPay sandbox
account before trusting them.
