const backImg=document.querySelector('.backImg')

document.addEventListener('scroll',function(){
    let value = window.scrollY
    console.log(value)
    if(value>2272 & value<3000){
        backImg.style.marginTop=(value-2772)/9+'px'
    }
})

document.addEventListener('pointermove',function () {
    let value =PointerEvent.window
    console.log(value)
})

