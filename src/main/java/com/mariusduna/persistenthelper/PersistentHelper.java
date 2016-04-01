package com.mariusduna.persistenthelper;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.mariusduna.persistenthelper.annotations.Persistent;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.logging.Level;

/**
 * Created by marius.duna on 01/04/16.
 */
public class PersistentHelper {
    private static final String TAG = "Persist";

    private PersistentHelper() {
    }

    public static boolean saveState(Object object, Bundle outState) {
        return saveState(object, outState, null);
    }

    public static boolean saveState(Object object, Bundle outState, Class baseClass) {
        if (object != null && outState != null) {
            Class current = null;
            while (true) {
                if (current != null) {
                    current = current.getSuperclass();
                } else {
                    current = object.getClass();
                }
                for (Field field : current.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Persistent.class)) {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "saving: " + field.getName());
                        }
                        boolean isAccessible = field.isAccessible();
                        if (!isAccessible) {
                            field.setAccessible(true);
                        }
                        try {
                            Object data = field.get(object);
                            if (data instanceof Parcelable) {
                                outState.putParcelable(field.getName(), (Parcelable) field.get(object));
                            } else if (data instanceof Serializable) {
                                outState.putSerializable(field.getName(), (Serializable) field.get(object));
                            }
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, "saved: " + field.getName() + " " + data);
                            }
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            if (BuildConfig.DEBUG) {
                                Log.e(TAG, field.getName(), ex);
                            }
                        }
                        if (!isAccessible) {
                            field.setAccessible(false);
                        }
                    }
                }
                if (baseClass == null || current == baseClass) {
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean restoreState(Object object, Bundle savedInstanceState) {
        return restoreState(object, savedInstanceState, null);
    }

    /**
     * Will unwrap the given class member from the bundle passed, by passing it's member declared name.
     *
     * @param memberName         the name of the field requested from the bundle
     * @param savedInstanceState the actual bundle
     * @return the saved member value
     */
    public static Object restoreMember(String memberName, Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(memberName) && savedInstanceState != null) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "restoring member: " + memberName);
            }

            Object data = null;
            try {
                try {
                    data = savedInstanceState.getSerializable(memberName);
                } catch (Exception e) {
                    data = null;
                }
                try {
                    if (data == null) {
                        data = savedInstanceState.getParcelable(memberName);
                    }
                } catch (Exception e) {
                    data = null;
                }


                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "restored member: " + memberName + " " + data);
                }
            } catch (IllegalArgumentException e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, memberName, e);
                }

            }
            return data;
        }
        return null;
    }

    public static boolean restoreState(Object object, Bundle savedInstanceState, Class baseClass) {
        if (object != null && savedInstanceState != null) {
            Class current = null;
            while (true) {
                if (current != null) {
                    current = current.getSuperclass();
                } else {
                    current = object.getClass();
                }
                for (Field field : current.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Persistent.class)) {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "restoring: " + field.getName());
                        }
                        boolean isAccessible = field.isAccessible();
                        if (!isAccessible) {
                            field.setAccessible(true);
                        }
                        try {
                            Object data = null;
                            if (savedInstanceState.containsKey(field.getName())) {
                                data = savedInstanceState.get(field.getName());
                                if (data instanceof Parcelable || data instanceof Serializable) {
                                    //do nothing , data of the right type
                                } else {
                                    data = null;
                                }
                            }
                            field.set(object, data);
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, "restored: " + field.getName() + " " + data);
                            }
                        } catch (Exception ex) {
                            if (BuildConfig.DEBUG) {
                                Log.e(TAG, "restoreState: " + field.getName(), ex);
                            }
                        }
                        if (!isAccessible) {
                            field.setAccessible(false);
                        }
                    }
                }
                if (baseClass == null || current == baseClass) {
                    break;
                }
            }
            return true;
        }
        return false;
    }
}
