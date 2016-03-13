function check(name, url, onComplete) {
	$.ajax({
		url : url,
		data : {
			'name' : name
		},
		type : 'GET',
		success : function(data) {
			var count = JSON.parse(data).count;
			if (count > 0)
				onComplete(false);
			else
				onComplete(true);
		}
	});
}