from __future__ import unicode_literals

from django.db import models
from django.contrib.auth.models import User

# Create your models here.


class CampUser(models.Model):  # The main campUser model includes the builtIn user.
    user = models.OneToOneField(User)

    CAMPER = 'camper'
    COUNSELLOR = 'counsellor'
    ADMINISTRATOR = 'administrator'
    ROLE_CHOICES = ((CAMPER, 'camper'), (COUNSELLOR, 'counsellor'), (ADMINISTRATOR, 'administrator'))

    role = models.CharField(max_length=10, choices=ROLE_CHOICES, default=CAMPER)
