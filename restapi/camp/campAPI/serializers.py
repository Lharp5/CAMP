from django.contrib.auth.models import User
from rest_framework import serializers

from models import CampUser, Membership, CampGroup


class CampGroupSerializer(serializers.ModelSerializer):
    description = serializers.CharField(allow_blank=True)

    class Meta:
        model = CampGroup
        fields = ('id', 'name', 'description')


class MembershipUserSerializer(serializers.ModelSerializer):
    first_name = serializers.CharField(source='member.user.first_name')
    last_name = serializers.CharField(source='member.user.last_name')

    class Meta:
        model = Membership
        fields = ('id', 'first_name', 'last_name')


class MembershipGroupSerializer(serializers.ModelSerializer):
    id = serializers.CharField(source='group.id')
    name = serializers.CharField(source='group.name')

    class Meta:
        model = Membership
        fields = ('id', 'name')


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('username', 'email')


class CampUserSerializer(serializers.ModelSerializer):
    first_name = serializers.CharField(source="user.first_name")
    last_name = serializers.CharField(source="user.last_name")
    username = serializers.CharField(source="user.username")
    email = serializers.CharField(source="user.email")
    password = serializers.CharField(source="user.password")

    class Meta:
        model = CampUser
        fields = ('id', 'first_name', 'last_name', 'username', 'password', 'email', 'role')

    def create(self, validated_data):
        user_data = validated_data.pop('user')
        user = User.objects.create_user(user_data.pop('username'), user_data.pop('email'), user_data.pop('password'),
                                        **user_data)
        camp_user = CampUser.objects.create(user=user, **validated_data)
        return camp_user
