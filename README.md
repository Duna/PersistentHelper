[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.me/dunatv) [![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2FDuna%2FPersistentHelper&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

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
