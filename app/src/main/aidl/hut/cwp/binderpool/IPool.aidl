// IPool.aidl
package hut.cwp.binderpool;

// Declare any non-default types here with import statements

interface IPool {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    IBinder queryBinder(int code);
}
