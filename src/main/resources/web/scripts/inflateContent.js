// script.js
document.addEventListener('DOMContentLoaded', () => {
    fetch('https://127.0.0.1:8443/getWebCardList')
        .then(response => response.json())
        .then(data => {
            createCardList(data.cards);
        })
        .catch(error => console.error('Error:', error));

    function createCardList(cards) {
        const cardContainer = document.getElementById('card-container');
        cards.forEach(card => {
            const cardElement = document.createElement('div');
            cardElement.className = 'card';
            cardElement.innerHTML = `
                <h2>${card.title}</h2>
                <p>${card.text}</p>
            `;
            cardContainer.appendChild(cardElement);
        });
    }
});
