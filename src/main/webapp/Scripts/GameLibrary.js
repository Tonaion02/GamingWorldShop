//function to format price
let USDollar = new Intl.NumberFormat('en-US', {
	    style: 'currency',
	    currency: 'USD',
	});
//function to format price

$(document).ready(function() {
	//format prices
	let classes = document.getElementsByClassName("price")
	for(let i = 0; i < classes.length; i++){
		classes[i].innerHTML = USDollar.format(classes[i].innerHTML/100);
	}
	//format prices
});