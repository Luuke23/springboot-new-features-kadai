const stripe = Stripe('pk_test_51QbDfPRwmcTnHJhn9FXGrK7SCAT39SaQj5Oj6wHEFd59c2szxLn4ACeMqBo9J5rTMthKEJ9ud5qIUCOKYrQnt5UM00yFRCW1vX');
 const paymentButton = document.querySelector('#paymentButton');
 
 paymentButton.addEventListener('click', () => {
   stripe.redirectToCheckout({
     sessionId: sessionId
   })
 });