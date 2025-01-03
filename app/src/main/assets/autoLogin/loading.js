const loaderHolder = document.createElement('div');
loaderHolder.className = 'loader-holder';
document.body.appendChild(loaderHolder);

const loader = document.createElement('div');
loader.className = 'loader';
loaderHolder.appendChild(loader);

addEventListener('load', () => {
    document.body.removeChild(loaderHolder);
})