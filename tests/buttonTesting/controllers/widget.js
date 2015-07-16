var button = Titanium.UI.createButton({
   title: 'Hello',
   top: 10,
   width: 100,
   height: 50
});

button.addEventListener('click', function(e) {
   Titanium.API.info("You clicked the button");
});