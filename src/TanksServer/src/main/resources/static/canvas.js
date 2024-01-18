var squareEnemy = {
     x: 50,
     y: 100,
     width: 70,
     height: 70
};

var square = {
    x: 50,
    y: 450,
    width: 70,
    height: 70
};

var canvas;
var ctx;
var imageEnemy = new Image();
var image = new Image();
var imageBullet1 = new Image();
var imageBullet2 = new Image();
var backgroundImage = new Image();
var hp_high = new Image();
var hp_low = new Image();
var hp_midl = new Image();
var bullets = [];
var opBullets = [];

var speedBullet = 10;
var animateDelay = 100;
var myHealth = 3;
var opHealth = 3;
var out = 0;

image.src = 'images/tank-player1.png';
imageEnemy.src = 'images/tank-player2.png';
imageBullet1.src = 'images/bullet.png';
imageBullet2.src = 'images/bulletRev.png';
backgroundImage.src = 'images/background.jpeg';
hp_high.src = 'images/hp_high.png';
hp_midl.src = 'images/hp_midl.png';
hp_low.src = 'images/hp_low.png';

document.addEventListener('DOMContentLoaded', function() {

    canvas = document.getElementById('battle-field');
    ctx = canvas.getContext('2d');
    canvas.width = 850;
    canvas.height = 600;

    var canShoot = true;
    var shootDelay = 1000;

    image.addEventListener('load', function() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.save();
        ctx.drawImage(backgroundImage, 0, 0, canvas.width, canvas.height);
        drawPlayers(ctx, canvas);
        drawMonitoring(ctx, canvas);
    });

    document.addEventListener('keydown', function(event) {
        var step = 10;
        var keyCode = event.keyCode;


        if (keyCode === 37) {
            if (square.x > 0) {
                square.x -= step;
            }
        } else if (keyCode === 39) {
            if (square.x < 850) {
                square.x += step;
            }
        } else if (keyCode === 16) {
            if (canShoot) {
                var x = square.x;
                var y = square.y;
                var bullet = { image: imageBullet1, x: x + 30, y: y - 7, width: 10, height: 20, speed: speedBullet, delay: animateDelay };
                bullets.push(bullet);
                sendCoordinatesBullets(bullet);
                canShoot = false;
                setTimeout(function() { canShoot = true; }, shootDelay);
            }
        }
        drawBullets(ctx, bullets);
        drawBullets(ctx, opBullets);
    });
    setInterval(function() {animate(ctx, canvas)}, animateDelay);
});

function updateBullets() {
  bullets.forEach(function(bullet) {
    bullet.y -= bullet.speed;

    if (bullet.y < 0) {
      var index = bullets.indexOf(bullet);
      bullets.splice(index, 1);
    }
  });
}

function drawBullets(ctx,  bullets) {
    bullets.forEach(function(bullet) {
        ctx.drawImage(bullet.image, bullet.x, bullet.y, bullet.width,bullet.height);
    });
}

function drawPlayers(ctx, canvas) {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.drawImage(backgroundImage, 0, 0, canvas.width, canvas.height);
    ctx.drawImage(image, square.x, square.y, square.width, square.height);
    ctx.drawImage(imageEnemy, squareEnemy.x, squareEnemy.y, squareEnemy.width, squareEnemy.height);
}

function drawMonitoring(ctx, canvas) {
    ctx.font="40px Courier New";
    ctx.fillText(localStorage.getItem('MyName') + ':', 50, 60, 100);
    ctx.fillText(localStorage.getItem('opName') + ':', 500, 60, 100);
    drawHealthPoint();
    drawCongratulation();
}

function drawCongratulation() {
    ctx.font="100px Verdana";
    switch (out) {
        case 1:
            ctx.fillText("You Lose", 200, 325);
            break;
        case 2:
            ctx.fillText("You Win", 200, 325);
            break;
        default:
            break;
    }
}

function drawHealthPoint() {
    switch (myHealth) {
        case 1:
            ctx.drawImage(hp_low, 170, 40, 130, 20);
            break;
        case 2:
            ctx.drawImage(hp_midl, 170, 40, 130, 20);
            break;
        case 3:
            ctx.drawImage(hp_high, 170, 40, 130, 20);
            break;
        default:
            ctx.drawImage(hp_high, 170, 40, 130, 20);
            break;
    }
    switch (opHealth) {
        case 1:
            ctx.drawImage(hp_low, 620, 40, 130, 20);
            break;
        case 2:
            ctx.drawImage(hp_midl, 620, 40, 130, 20);
            break;
        case 3:
            ctx.drawImage(hp_high, 620, 40, 130, 20);
            break;
        default:
            ctx.drawImage(hp_high, 620, 40, 130, 20);
            break;
    }


}


function animate(ctx, canvas) {
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  ctx.drawImage(backgroundImage, 0, 0, canvas.width, canvas.height);
  drawPlayers(ctx, canvas);
  drawMonitoring(ctx, canvas);
  updateBullets();
  updateOpBullets();
  drawBullets(ctx, bullets);
  drawBullets(ctx, opBullets);
  requestAnimationFrame(animate);
}

function updateOpBullets() {
  opBullets.forEach(function(opBullet) {
    opBullet.y += opBullet.speed;

    if (opBullet.y > 800) {
      var index = opBullets.indexOf(opBullet);
      opBullets.splice(index, 1);
    }
  });
}
