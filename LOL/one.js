const backImg=document.querySelector('.backImg')

document.addEventListener('scroll',function(){
    let value = window.scrollY
    console.log(value)
    if(value>2000 & value<3000){
        backImg.style.marginTop=(value-2400)/9+'px'
    }
})

const mousemove=document.addEventListener('mousemove',function (e) {
    document.querySelectorAll(".backImg1").forEach(function (move){
        var moving_value=move.getAttribute("data-value")
        var x= (e.clientX*moving_value)/250
        var Y= (e.clientY*moving_value)/250
        move.style.transform= "translateX("+x+"px) translateY("+Y+"px)"
        move.style.transition="0.3s"
    })
})
const mousemove1=document.addEventListener('mousemove',function (e) {
    document.querySelectorAll(".backImg2").forEach(function (move){
        var moving_value=move.getAttribute("data-value")
        var x= (e.clientX*moving_value)/300
        var Y= (e.clientY*moving_value)/300
        move.style.transform= "translateX("+-x+"px) translateY("+Y+"px)"
        move.style.transition="0.3s"
    })
})
