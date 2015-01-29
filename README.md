# code-meanings
Code Meanings is an Android app to manage code snippets and their meanings. The project is an introduction to cloud storage, with two separate implementations. It runs on Android Lollipop. 

##Google Endpoints and Custom backend##
The first method of cloud storage uses <a href="https://cloud.google.com/appengine/docs/java/endpoints/">Google Cloud endpoints</a>. The custom backend implements CRUD functionality for a custom cloud-based API that returns data in JSON format.

To run each implementation, in the AndroidManifest.xml, set either appengine or custom MainActivity as the launcher activity. 

To get the Google Cloud endpoints running, you'll need to set up a project on Google AppEngine and set the project name in the backend module appengine-web.xml. 

For the custom implementation, you'll need to create an API in your web application to expose your data.
