
######################################## chat application  ############################################
							 


# Problématique:
comment  créer une salle de discussion dans laquelle plusieurs utilisateurs peuvent communiquer ensemble?

# Solution:
Application java utilisant les sockets mode non connecter UDP (User Datagram Protocol)

Les Services offre Par Application :

1. Enregistrement :

Le serveur est interconnecté avec une base de données dans laquelle les utilisateurs
seront déclarés. Lorsque l’utilisateur demande d’être enregistrer le serveur ajoute un
enregistrement dans la table utilisateur.

2. Demande de session :

Utilisateur demande au Serveur d’être parmi la liste des utilisateurs connecter pour
recevoir et envoyer des messages et des invitations.

3. Ouverture de session :

Chaque utilisateur connecter a une session dans laquelle il peut envoyer et recevoir des
message. L’utilisateur envoi le message et la destination de message au Serveur et le
serveur transfère ce message à sa destination finale.
Utilisateur peut envoyer et recevoir des invitation, et consulter la liste des amis durant
sa connexion a traves la session que le serveur lui accorder.

############################################# Pour utiliser application ##################################

# importer le fichier de base de données

=======> chat_application.sql

 # lancer le serveur
 
=======> exécuter la classe Server.java

#  lancer les clients 

========> exécuter la classe chatClient.java

############################################################# démonstration #######################################

![cap1](https://user-images.githubusercontent.com/56040047/212101293-47f928d8-ca6f-4db6-8aca-d90136efe488.png)

![ca5](https://user-images.githubusercontent.com/56040047/212102737-df4af97f-9782-45a2-b539-fd8f98fc2c8b.png)

![cap2](https://user-images.githubusercontent.com/56040047/212102752-921c33db-61e5-4a33-a1c6-2ba040e39abd.png)

![cap3](https://user-images.githubusercontent.com/56040047/212102755-96bea473-b5f5-41ac-a790-0ffb20e18932.png)

![cap4](https://user-images.githubusercontent.com/56040047/212102760-fab847f7-790f-4d66-8235-84762f171e72.png)






