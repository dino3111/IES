As mensagens consumidas são removidas da queue depois de serem entregues ao consumidor.
Como tem muitos producers, as mensagens são acumuladas na queue por ordem de chegada, e são distribuídas entre eles.
As antigas permanecem na queue até serem consumidas, depois disso, são eliminadas.