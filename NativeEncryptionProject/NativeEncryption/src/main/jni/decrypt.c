#include <string.h>
#include <jni.h>
#include <openssl/evp.h>
#include <android/log.h>

EVP_CIPHER_CTX *d_ctx;

void Java_gsn_atl_nativeencryption_Decryption_initCipher(JNIEnv* env, jobject obj, jbyteArray key, int keylen, jbyteArray iv, int ivlen){
    jbyte keyarr[keylen];
    (*env)->GetByteArrayRegion(env, key, 0, keylen, keyarr);

    jbyte ivarr[ivlen];
    (*env)->GetByteArrayRegion(env, iv, 0, ivlen, ivarr);

    d_ctx = EVP_CIPHER_CTX_new();
    EVP_CIPHER_CTX_init(d_ctx);
    EVP_DecryptInit_ex(d_ctx, EVP_aes_128_ecb(), NULL, keyarr, ivarr);
}

void Java_gsn_atl_nativeencryption_Decryption_update(JNIEnv* env, jobject obj, jbyteArray in, int len, jbyteArray out)
{
    jbyte encrypted[len];
    (*env)->GetByteArrayRegion(env, in, 0, len, encrypted);

    EVP_DecryptInit_ex(d_ctx, NULL, NULL, NULL, NULL);

    int bytes_written = 0;
    unsigned char decrypted[len];

    EVP_DecryptUpdate(d_ctx, decrypted, &bytes_written, encrypted, len);

    (*env)->SetByteArrayRegion(env, out, 0, len, (jbyte*)decrypted);
}

void Java_gsn_atl_nativeencryption_Decryption_finalize(JNIEnv* env, jobject obj, jbyteArray in, int len, jbyteArray out)
{
    jbyte encrypted[len];
    (*env)->GetByteArrayRegion(env, in, 0, len, encrypted);

    EVP_DecryptInit_ex(d_ctx, NULL, NULL, NULL, NULL);

    int bytes_written = 0;
    unsigned char decrypted[len];

    EVP_DecryptUpdate(d_ctx, decrypted, &bytes_written, encrypted, len);
    EVP_DecryptFinal_ex(d_ctx, decrypted + bytes_written, &bytes_written);

    (*env)->SetByteArrayRegion(env, out, 0, len, (jbyte*)decrypted);
    EVP_CIPHER_CTX_cleanup(d_ctx);
}