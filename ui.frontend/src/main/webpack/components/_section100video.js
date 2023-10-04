
const Section100Video = {
    selectors: {
        self: '[data-cmp-component="section100video"]',
        videoElement: 'video',
        videoControlBtn: 'button',
        videoControlText: 'button span',
    },
    init: function () {
        console.log('Section100%: Video init');
        const {selectors} = this;
        const videoSections= document.querySelectorAll(selectors.self);
        videoSections.forEach(section => {
            const video = section.querySelector(selectors.videoElement);
            const  videoControl = section.querySelector(selectors.videoControlBtn);
            const videoControlText = section.querySelector(selectors.videoControlText);
              videoControl.addEventListener('click', function(){
                if (video.paused) {
                  video.play(); 
                  videoControl.classList.remove("btn-videoControl--play");
                  videoControl.classList.add("btn-videoControl--pause");
                  videoControlText.textContent="Pause the video";
                } else {
                  video.pause();
                  videoControl.classList.remove("btn-videoControl--pause");
                  videoControl.classList.add("btn-videoControl--play");
                  videoControlText.textContent="Play the video";
                }
              });
        });
    },
};
export default Section100Video;
