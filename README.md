# CVMTools
Android app with helpful tools for viticulture technician at Clark Vineyard Management. These include, but not limited to, a row vine count export, spray work order pdf creator, vehicle inspection checklist, and crop disease image labeling (concept).

## Walkthrough 
Upon opening application, users select which task they would like.

### Row Vine Count
For a row vine count task, users select which vineyard they are at. The blocks for that vineyard appear and user can edit each one individually. Row number and vine count are requiered and user presses enter button on keyboard to add the row vine count to master list. Users can delete or overwrite existing rows. User must press save and then screen returns to block list and modified blocks have a green checkmark icon. Not saved/modified blocks have an orange X. Once user is ready to share, they press share button and can send the generated .csv file as an email attachment, or upload to cloud.

<img src="https://github.com/juanm707/CVMTools/blob/master/examples/row_vine_counts.gif" width=230 height=500 /><img src="https://github.com/juanm707/CVMTools/blob/master/examples/email_rvc.png" width=500 height=300 /><img src="https://github.com/juanm707/CVMTools/blob/master/examples/rvc_csvsheet.png" width=350 height=275 />

### Generate Work Order
For generating a spray work order, users can select the vineyard, type of chemical use like fungicide, herbicide etc... Need to enter the main product used, when the spray is going to be, tank size and tanks, and special instructions. Users also need to add/remove the products use and blocks being sprayed. Once it is ready, users press the generate work order button, and opens up the pdf which can be downloaded or shared. 

<img src="https://github.com/juanm707/CVMTools/blob/master/examples/work_order_generator.gif" width=230 height=500 /><img src="https://github.com/juanm707/CVMTools/blob/master/examples/work_order_email.png" width=500 height=300 /><img src="https://github.com/juanm707/CVMTools/blob/master/examples/work_order_pdf.png" width=350 height=275 />

### Vehicle Inspection
For a vehicle inspection, there is a scrollable vehicle with different section buttons located throughout the vehicle. Each section corresponds to different checklist items. Once clicked, bottom sheet pops up and displays items that could be checked. Once checked, it saves it automatically. Once user is done, they should click the top right upload/share button, add any final comments, and then send.

<img src="https://github.com/juanm707/CVMTools/blob/master/examples/vehicle_checklist.gif" width=230 height=500 /><img src="https://github.com/juanm707/CVMTools/blob/master/examples/email_vehicle.png" width=500 height=300 /><img src="https://github.com/juanm707/CVMTools/blob/master/examples/vehicle_csvsheet.png" width=350 height=500 />

### Crop Disease Labeling (Not final)
This is a work in progress. Using Google's ML Kit for Android, user can upload an image from their phone and label the diease, if any, shown in the picture. Currrently uses the default the [default image labeling model which can identify general objects, places, activities, animal species, products, and more.](https://developers.google.com/ml-kit/vision/image-labeling) However, a custom one can be used for plants, and crop diseases like [this one](https://tfhub.dev/agripredict/lite-model/disease-classification/1) or [this one.](https://tfhub.dev/rishit-dagli/lite-model/plant-disease/default/1) The better model would include just grape leaves and also include berries, trunks, roots etc.

<img src="https://github.com/juanm707/CVMTools/blob/master/examples/crop_disease_labeling.gif" width=230 height=500 />

## Built with
- Kotlin
- Android Studio
- AndroidX Libraries
- Material Design
- Machine Learning Kit Android
- Coil

## Notes/Future
- Add more tasks.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
