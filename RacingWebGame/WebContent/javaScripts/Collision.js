/**
 * A hit map class for dynamically
 * checking whether an x/y coordinate
 * with an appropriate hit image
 *
 * @param {Image} img The hit map image
 */
function HitMap(img){
	var self = this;
	this.img = img;

	if (img.complete){
		this.draw();
	} else {
		img.onload = function(){
			self.draw();
		};
	}
}
HitMap.prototype = {
	draw: function(){
		// first create the canvas
		this.canvas = document.createElement('canvas');
		this.canvas.width = this.img.width;
		this.canvas.height = this.img.height;
		this.context = this.canvas.getContext('2d');
		// draw the image on it
		this.context.drawImage(this.img, 0, 0);
	},
	isHit: function(x, y, rotation){
        if (this.context){
        	var dx = parseInt((Math.cos(rotation * TO_RADIANS) * 10));
        	var dy = parseInt((Math.sin(rotation * TO_RADIANS) * 19));
        	var absoluteX = x*1 + dx*1;
        	var absoluteY = y*1 + dy*1;
            var pixel = this.context.getImageData(absoluteX*1, absoluteY*1, 1, 1);
            if (pixel){
               return pixel.data[0];
            } else {
                return 256;
            }
        } else {
            return 256;
        }
	}
};

var TO_RADIANS = Math.PI/180;

var canvas   = document.getElementById('canvas'),
	context  = canvas.getContext('2d'),
	ctxW     = canvas.width,
	ctxH     = canvas.height,
	track    = new Image(),
	trackHit = new Image();

track.src = "Images/trasa.png";
trackHit.src = "Images/trasaHitMap.png";

var hit = new HitMap(trackHit);
