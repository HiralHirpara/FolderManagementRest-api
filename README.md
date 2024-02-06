# About the project

Folder Management REST API developed using java and springboot framework.

## API workflow :

List all top-level files and folders.
List all files and folders in an existing folder.
Create new top level folder.
Create a new folder in an existing folder.
Upload a file to the top level (i.e. no parent folder).
Upload a file to an existing folder - no file in the folder with the same name.
Upload a file to an existing folder - there is a file in the folder with the same name.
Download a specific version of a file.
Download a file without specifying a version.
Delete a file.
Delete a folder.


## API endpoints :

GET : /folders
GET : /folders/{folderid}
GET : /files/download/{id}
GET : /files/version/{id}

POST : /folders
POST : /folders/{folderid}
POST : /files
POST : /files/{id}

DELETE : /folder/{id}
DELETE : /files/{id}

