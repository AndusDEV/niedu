showLoader();

function showLoader() {
	const style = document.createElement('style');
	style.textContent = `
    .loader {
    	width: 45px;
    	aspect-ratio: 1;
    	--c: no-repeat linear-gradient(#ca2c2a 0 0);
    	background: var(--c) 0% 50%, var(--c) 50% 50%, var(--c) 100% 50%;
    	background-size: 20% 100%;
    	animation: l1 1s infinite linear;
    }
    .loader-holder {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        background: rgba(0, 0, 0, 0.7);
        z-index: 9999;
    }
    @keyframes l1 {
    	0% {
    		background-size: 20% 100%, 20% 100%, 20% 100%;
    	}
    	33% {
    		background-size: 20% 10%, 20% 100%, 20% 100%;
    	}
    	50% {
    		background-size: 20% 100%, 20% 10%, 20% 100%;
    	}
    	66% {
    		background-size: 20% 100%, 20% 100%, 20% 10%;
    	}
    	100% {
    		background-size: 20% 100%, 20% 100%, 20% 100%;
    	}
    }
`;
	(document.head || document.documentElement).appendChild(style);

	const loaderHolder = document.createElement('div');
	loaderHolder.className = 'loader-holder';
	const loader = document.createElement('div');
	loader.className = 'loader';
	loaderHolder.appendChild(loader);

	(document.body || document.documentElement).appendChild(loaderHolder);

	window.addEventListener('load', () => {
		loaderHolder.remove();
	});
}