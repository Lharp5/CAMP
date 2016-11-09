from django.contrib.auth.models import User, Group
from rest_framework import serializers

from models import CampUser


class GroupSerializer(serializers.ModelSerializer):
    class Meta:
        model = Group
        fields = ('id', 'name')


class UserSerializer(serializers.ModelSerializer):
    groups = GroupSerializer(many=True)
    class Meta:
        model = User
        fields = ('username', 'email', 'groups')


class CampUserSerializer(serializers.ModelSerializer):
    first_name = serializers.CharField(source="user.first_name")
    last_name = serializers.CharField(source="user.last_name")
    username = serializers.CharField(source="user.username")
    email = serializers.CharField(source="user.email")
    password = serializers.CharField(source="user.password")
    groups = GroupSerializer(many=True, source="user.groups")

    class Meta:
        model = CampUser
        fields = ('id', 'first_name', 'last_name', 'username', 'password', 'email', 'role', 'groups')

    def create(self, validated_data):
        user_data = validated_data.pop('user')
        user = User.objects.create_user(user_data.pop('username'), user_data.pop('email'), user_data.pop('password'),
                                        **user_data)
        camp_user = CampUser.objects.create(user=user, **validated_data)
        return camp_user
