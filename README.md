# MultiChoiceForm
Android Library [MultiChoiceForm](https://github.com/lcabrales/multichoiceform)

Easy implementation of a multi selection form element. It runs on [API level 16]
(https://developer.android.com/guide/topics/manifest/uses-sdk-element.html#ApiLevels) and upwards.

# Features

Here's a list of the MultiChoiceForm library core features as of the current version.

  * Include any amount of FormSteps in your layout.
  * Support for dependent fields.
  * Single selection only.
  * Change any FormStep's options data in runtime.
  * Set required fields (with validation animations).
  * Customize validation animation.
  * Customize OptionsActivity toolbar.
  * Customize options EmptyView text.
  * Customize FormStepView colors and drawable.

# Demo

Check out the videos of our sample app:

1. [Full Sample](https://github.com/lcabrales/multichoiceform/screenshots/sample.mp4)
2. [Validation only](https://github.com/lcabrales/multichoiceform/screenshots/validation_animation.mp4)

Or just check out the screenshots below:

    - **Main Form**
![alt tag](https://raw.githubusercontent.com/lcabrales/multichoiceform/master/screenshots/main_form.png)
    - **Options List**
![alt tag](https://raw.githubusercontent.com/lcabrales/multichoiceform/master/screenshots/options_list.png)
    - **Empty View**
![alt tag](https://raw.githubusercontent.com/lcabrales/multichoiceform/master/screenshots/options_empty_view.png)
    - **Main Form Selected**
![alt tag](https://raw.githubusercontent.com/lcabrales/multichoiceform/master/screenshots/main_form_selections.png)

# Import

Using Gradle, import the dependency into your project, add this into your project's build.gradle file:

```java
allprojects {
  repositories {
      ...
      maven {
          url  'https://dl.bintray.com/hypernovalabs/maven'
      }
  }
}
```

Then, in your app's build.gradle file:
```java
compile 'com.hypernovalabs:multichoiceform:0.2.0@aar'
```

# Usage

Here is a simple implementation of the MultiChoiceForm library. You can access the javadoc
[here](https://github.com/lcabrales/multichoiceform/javadoc/v0.2.0/index.html).
Add a FormStepView into your layout as follows:

```xml
<com.hypernovalabs.multichoiceform.view.FormStepView
            android:id="@+id/form_test"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:title="Test" />
```

Next, you have to define your FormSteps in your activity class. The parameters are as follows:
  * data - your ArrayList<String> containing the FormStep's options
  * formStepView - your FormStepView
  * required (optional) - default is false
  
Example:
```java
ArrayList<String> data = ... //your data
FormStep step = new FormStep(data, (FormStepView) findViewById(R.id.form_test), true);
```

Then, create an `ArrayList<FormStep>` and add all of your FormSteps into it:
```java
mFormSteps = new ArrayList<>();
mFormSteps.add(step);
mFormSteps.add(step2);
mFormSteps.add(step3);
...
```

Next, you have to instantiate a MultiChoiceForm helper using the class' Builder:

```java
MultiChoiceForm.Builder builder = new MultiChoiceForm.Builder(mContext);
builder.setSteps(mSteps); //required, ArrayList<FormStep> containing all of your FormSteps
mForm = builder.build(); //build your instance

mForm.setupForm(); //required in order to enable the FormSteps
```

Then, add this in your `onActivityResult`:
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    //Obligatory call
    mForm.handleActivityResult(requestCode, resultCode, data);
}
```

Finally, add the definition of `OptionsActivity` in your `AndroidManifest.xml`:
```xml
<activity android:name="com.hypernovalabs.multichoiceform.OptionsActivity"
            android:screenOrientation="portrait"/>
```

Optionally, to validate your data, call `mForm.validate();` in your "Submit" button,
if it returns true, all your required fields are filled.

That's it, you are all set!

## Extra Customization

This is all optional, adds increased customization to your form.

Customize the OptionsActivity and other MultiChoiceForm settings:

```java
MultiChoiceForm.Builder builder = new MultiChoiceForm.Builder(mContext);
builder.setSteps(mSteps); //required, ArrayList<FormStep> containing all of your FormSteps
builder.setToolbarColors(
    ContextCompat.getColor(mContext, R.color.toolbar),
    ContextCompat.getColor(mContext, R.color.toolbar_text)); //optional
builder.setRequiredText("Fill out everything, please"); //optional
builder.setValidationColor(ContextCompat.getColor(mContext, R.color.bluet)); //optional
builder.setValidationAnimation(ValidationAnim.SHAKE); //optional
builder.setValidationDuration(Duration.SHORT); //optional
builder.setEmptyViewTexts("Attention!", "Fill out all of the required fields, please"); //optional
mForm = builder.build(); //build your instance
```

You can fully customize the view in XML:

```xml
<com.hypernovalabs.multichoiceform.view.FormStepView
            android:id="@+id/form_test"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:arrowDrawable="@drawable/ic_arrow"
            app:enabled="false"
            app:titleColor="@color/blue"
            app:separatorColor="@color/gray"
            app:selectionColor="@color/blue"
            app:title="Empty" />
```
  
You can also customize it in your Java class:
  
```java
FormStep formStep = new FormStep(data, (FormStepView) findViewById(R.id.form_test), true);

formStep.getView().setTitle("Title");
formStep.getView().setTitleColor(Color.BLACK);
formStep.getView().setSelection("Selection");
formStep.getView().setSelectionColor(Color.RED);
formStep.getView().setSeparatorColor(ContextCompat.getColor(mContext, R.color.red));
formStep.getView().setArrowImageView(ContextCompat.getDrawable(mContext, R.drawable.ic_action_arrow));
formStep.getView().enable(false);
```

## Dependent FormSteps

If you need to set any dependent step (the data depends on the selection of a previous step),
you can change the FormStep data at runtime on your onActivityResult method.

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    //Obligatory call
    mForm.handleActivityResult(requestCode, resultCode, data);

    //In case a step depends on another step
    if (resultCode == RESULT_OK && requestCode == MultiChoiceForm.REQ_SELECTION) {
        int id = data.getIntExtra(OptionsActivity.ID_KEY, 0); //gets the resId of the selected FormStep

        //If you need to handle several dependent steps
        FormStep formStep;
        switch (id) {
            case R.id.form_test3:
                formStep = FormStep.getStepFromId(mSteps, id);

                if (formStep != null) {
                    mDependentStep.setData(getDataFromSelection(formStep.getView().getSelection(), 5));
                    // Removes any previous selection from both of the dependent FormSteps fields
                    // and enables them
                    mDependentStep.getView().deselect(true); // direct child, enables it
                    mDependentStep2.getView().deselect(); // only deselects, still has a parent deselected
                }

                break;
            case R.id.form_dependent:
                formStep = FormStep.getStepFromId(mSteps, id);

                if (formStep != null) {
                    mDependentStep2.setData(getDataFromSelection(formStep.getView().getSelection(), 5));
                    //Removes any previous selection and enables it.
                    mDependentStep2.getView().deselect(true);
                }

                break;
        }
    }
}
```
