# Remote Accounting Application

## Description
The Remote Accounting Application is the application that allows you to keep track of sales at remote locations without a permanent Internet connection. 

## Key Features
The Remote Accounting Application contains the following functionality: 
1) Easy tracking of sales and receipts
2) Export sales reports in CSV format
3) Operational data handbooks
4) Backup

minSdk = 27, targetSdk = 34, language = Russian (English is coming soon...)

## Technologies Used
* Android Architecture: UI, Model
* Room database: Entities, DAO, SQL queries
* Repository as an additional layer between the Room DB and ViewModel
* Runtime Permissions
* Writing files with File Output Stream
* Sharing files with FileProvider (ContentProvider)
* Importing files with Input Stream
* TextChangedListener
* AppCompatAutoCompleteTextView
* ViewModel
* LiveData
* Flow
* Coroutines
* Saved Instance State
* RecyclerView & MenuClickListener interface for providing click listening
* View Binding
* Navigation Graph
* Save Args
* ActionBarWithNavController
* Popup menu
* Options menu
* Scope functions: apply
* Constraint Layout
* AlertDialog & DialogFragment
* Date Picker
* Landscape View
* Snackbar
* Styles
* String Builder
* Light and Dark Themes
* Icon

## Detailed Description
The Remote Accounting Application is ideal for tracking sales in remote locations such as tourist bases, small stalls, or kiosks. It allows to write down sales, export them as a user-friendly CSV file, and transport the file over a network with any messenger or email for further processing in the office. It is very convenient for places with poor network connections because it does not require a network connection for any operations except CSV file sharing. Moreover, a copy of the report is saved locally on the device, and it is possible to transfer it later at any time when required.

* The first page of the app shows a filling form for a sale registration. All fields except comments are mandatory. Name, product, sale type, and payment type fields will remember written text and will propose it.
<img src="https://github.com/user-attachments/assets/cf9b799b-9594-4992-bc55-6a80fc42a6fb" width="285" height="627">
<img src="https://github.com/user-attachments/assets/25bfc6c5-afb4-4df2-b850-9f76ee64671a" width="283" height="360">
<img src="https://github.com/user-attachments/assets/20d6f050-04fb-46ac-8366-9d7ccc87a4cc" width="284" height="363">

<img src="https://github.com/user-attachments/assets/adf37729-c192-4b20-ab08-9d10c2c4fd01" width="285" height="624">
<img src="https://github.com/user-attachments/assets/3ad61705-93e4-4fda-8724-449b994e0c23" width="283" height="627">
<img src="https://github.com/user-attachments/assets/8c0bbf4c-2877-4fae-aca2-9533df587b55" width="284" height="624">

* On the sales page, all sales can be seen sorted by the date. Choose another date through a clicking next day/previous day button or by choosing a date through a calendar date picker.

![Screenshot 2024-08-31 131334](https://github.com/user-attachments/assets/1dbf1113-88c4-44b6-81b4-42cb4727165b)
<img src="https://github.com/user-attachments/assets/a9422a85-86ad-47c2-bbce-db6ad3aa939b" width="290" height="626">
<img src="https://github.com/user-attachments/assets/5b6a659a-6c5a-4d0f-aacd-f0fc6e84f398" width="287" height="628">

* If the user wants to be aware of how much product is still left in the warehouse, it is possible to arrange the receipt of the product to the warehouse.
<img src="https://github.com/user-attachments/assets/756c624c-b1a1-4545-969a-7e143fc3ad72" width="284" height="623">
<img src="https://github.com/user-attachments/assets/4bf71ba7-ebb5-4bf0-ac3e-7834e2b872fd" width="284" height="626">

* Additionally, it is possible to see all received items sorted by a date.
<img src="https://github.com/user-attachments/assets/82b1a19c-96f6-4c4a-91e1-d632b09e9f4f" width="284" height="627">

* It is possible to export a sales report as a CSV file. It can be done as a default 30-day report, or a user can choose dates on their own.
<img src="https://github.com/user-attachments/assets/7e1e5e3a-83ba-4942-9082-110b190c14a6" width="284" height="623">
<img src="https://github.com/user-attachments/assets/407c1591-6cf6-42d9-9f68-34749fb8c4fe" width="284" height="629">

* A report can be sent by using installed messengers, and it is also saved in the Downloads directory on the device.
<img src="https://github.com/user-attachments/assets/d64da5e6-02eb-48b0-a93a-c50188980e28" width="285" height="624">
<img src="https://github.com/user-attachments/assets/48069810-1122-4530-b459-6ab41c81f8fa" width="288" height="187">
<img src="https://github.com/user-attachments/assets/5804dab3-c125-4985-8682-0a9e8288fd66" width="728" height="328">

* Handbooks store operational data such as product titles, names of the app users, sale types, and payment types. All the data is saved in a single copy without duplicates and is used in other parts of the app for automatic autocomplete and to count the quantity of goods. All handbooks are filled automatically.
<img src="https://github.com/user-attachments/assets/998abb34-7e20-4818-b1a3-2dbd5cda0996" width="281" height="623">

* The app uses the Room DB, which is closely related to the app and is deleted when the app is uninstalled. So it may be critical to be able to save data if the app is uninstalled. To provide this functionality, the backup function is implemented. The backup is performed automatically with every fresh start of the app, and additionally, it is possible to create a backup manually through settings.
<img src="https://github.com/user-attachments/assets/f7254884-333b-453d-ad90-6c45f20f30fd" width="281" height="624">
<img src="https://github.com/user-attachments/assets/3a905bc6-c0cc-4566-8aeb-e06affc8e8bd" width="288" height="626">
<img src="https://github.com/user-attachments/assets/46911a8c-ebf0-42e4-b95d-3c7e7ff1691a" width="281" height="623">
<img src="https://github.com/user-attachments/assets/e853bdd6-5682-4532-abce-96e9a673f703" width="285" height="626">

* After backup creation, it is possible to restore database tables with a restore function.
<img src="https://github.com/user-attachments/assets/56685609-334c-46d5-9bf1-fba1a6140f0c" width="285" height="625">
