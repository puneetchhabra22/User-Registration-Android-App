# Android User Registration App

This Android app allows new users to sign up and existing users to login via their credentials. Firebase Realtime Database is used at the backend to store the users information.

### Key Features
- User registration and login with email and password
- Checks for existing users, empty text fields and does password matching.
- Uses intents, intents put extra, companion objects, Firebase, and has a custom UI.

### Screenshots of the app
![Screenshots](https://github.com/puneetchhabra22/User-Registration-Android-App/assets/142248901/1d2a976c-090e-4d3e-904e-373dd69f01f9)

## Key Learnings

### Learned about asynchronus and synchronus operations

Goal was to chek the user existance in the database and if the user if already registered check the passwords and login the autherised users.

For help I have used a checkUser method, to first check the username in database and then:
- If user exist, check password
- - if password is correct, return true.
  - if password is wrong, shows a toast.
- If the user doesn't exists, return false.

The follwing code uses the checkUser method and if the user is authorised, take the user to home screen.  
```
checkUser(username,password){success ->
    if(success){
        val homeIntent = Intent(this,HomeActivity::class.java)
        databaseUsersReference.child(username).get().addOnSuccessListener { user ->
            val name = user.child("name").value.toString()
            val email = user.child("email").value.toString()
            Log.i("putExtra", "got name: "+name)
            Log.i("putExtra", "got email: "+email)
            homeIntent.putExtra(NAME, name)
            homeIntent.putExtra(EMAIL, email)
        }
        startActivity(homeIntent)
    } else { //user doesn't exist
        val signUpIntent = Intent(this,SignUpActivity::class.java)
        startActivity(signUpIntent)
    }
}
```
Everything works fine but the home activity instead of showing user data, it shows null. But if you check via log statements, there is no error while retrieving the data from firebase, it is successfully retrieved.

So why does extras has a NULL value. Here's why:
The databaseUsersReference.child(username).get().addOnSuccessListener() call is an asynchronous operation. This means that it does not block the main thread and the code below it continues to execute.
The startActivity() call is executed before the databaseUsersReference.child(username).get().addOnSuccessListener() call has finished retrieving the user's name and email. This is why the name and email extras are null when the HomeActivity is started.

Moving the startActivity() call inside the addOnSuccessListener() callback ensures that it is not executed until the user's name and email have been retrieved from the database and set as extras on the homeIntent. So the following code is the correct one:
```
checkUser(username,password){success ->
    if(success){
        val homeIntent = Intent(this,HomeActivity::class.java)
        databaseUsersReference.child(username).get().addOnSuccessListener { user ->
            val name = user.child("name").value.toString()
            val email = user.child("email").value.toString()
            Log.i("putExtra", "got name: "+name)
            Log.i("putExtra", "got email: "+email)
            homeIntent.putExtra(NAME, name)
            homeIntent.putExtra(EMAIL, email)
            startActivity(homeIntent)
        }
    } else { //user doesn't exist
        val signUpIntent = Intent(this,SignUpActivity::class.java)
        startActivity(signUpIntent)
    }
}
```
### Learned about using companion objects for Intent putExtras keys and why
While using putExtra we pass key value pair. The key can be hardcoded at the same line where the putExtra statement is written like intent.putExtra("KEY",value). But it is always a good practice to make companion objects like 
```
companion object {
    const val NAME = "com.example.loginsignupscreen.NAME"
    const val EMAIL = "com.example.loginsignupscreen.EMAIL"
}
```
and then use them. This is done to improve maintainability and avoid typos. And also the value of this companion object can be anything like

```
//bad practice
companion object {
    const val NAME = "Name"
    const val EMAIL = "Email"
}
```
But we use package names here as prefix to avoid conflicts between different apps and libraries as used above : const val NAME = "com.example.loginsignupscreen.NAME"

### How data is stored in firebase realtime database
It is stored in form of JSON objects. When we add user(data) to the JSON tree, it becomes a node in the existing JSON structure with an associated key(username).
![firebase](https://github.com/puneetchhabra22/User-Registration-Android-App/assets/142248901/312acc2d-e4e3-4a8e-acc7-be240186a760)

## Future Scope
- Encrypted passwords
  - Storing password's hash value instead of the password itself
- Trimming leading and trailing the input user fields
- Making input fields as not case sensitive
