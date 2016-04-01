# Persistent Helper
Easy way to persist any objects when Android screen rotation

![Logo](persistent.png)

By using this library you eliminate lot of code that is used for saving data when screen orientation changes

Use the following code to save and restore data:

```java
public class ExampleFragment extends Fragment {
    @Persistent
    private Object javaObject;
    @Persistent
    private int currentPosition;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PersistentHelper.saveState(this, outState, Fragment.class);
        //javaObject and currentPosition saved
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PersistentHelper.restoreState(this, savedInstanceState, Fragment.class);
        //javaObject and currentPosition restored
    }
    ```
    
    For additional information mailto: dunatv@gmail.com
