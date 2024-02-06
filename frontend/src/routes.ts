import { type FC } from "react";
import {
  HomePage,
  LoginPage,
  UserProfilePage,
  RentcaseListPage,
  UserListPage,
  LesseeFormPage,
  LessorFormPage,
  LessorCreationFormPage,
  LesseeCreationFormPage,
  RentCaseFormPage,
  RentCaseCreationFormPage,
} from "./pages";

interface Route {
  key: string;
  title: string;
  path: string;
  enabled: boolean;
  component: FC<any>;
}

export const routes: Route[] = [
  {
    key: "home-page-route",
    title: "Home",
    path: "/",
    enabled: true,
    component: HomePage,
  },
  {
    key: "login-page-route",
    title: "Login",
    path: "/login",
    enabled: true,
    component: LoginPage,
  },
  {
    key: "profile-page-route",
    title: "Profile",
    path: "/profile",
    enabled: true,
    component: UserProfilePage,
  },
  {
    key: "rentcase-list-page-route",
    title: "RentCases",
    path: "/rentcases",
    enabled: true,
    component: RentcaseListPage,
  },
  {
    key: "user-list-page-route",
    title: "Users",
    path: "/users",
    enabled: true,
    component: UserListPage,
  },
  {
    key: "lessee-form-page-route",
    title: "Lessee Edit",
    path: "/lessees/:id?",
    enabled: true,
    component: LesseeFormPage,
  },
  {
    key: "lessor-form-page-route",
    title: "Lessor Edit",
    path: "/lessors/:id?",
    enabled: true,
    component: LessorFormPage,
  },
  {
    key: "lessor-create-form-page-route",
    title: "Lessor Creation",
    path: "/users/newlessor",
    enabled: true,
    component: LessorCreationFormPage,
  },
  {
    key: "lessee-create-form-page-route",
    title: "Lessee Creation",
    path: "/users/newlessee",
    enabled: true,
    component: LesseeCreationFormPage,
  },
  {
    key: "rentcase-form-page-route",
    title: "RentCase Edit",
    path: "/rentcases/:rentcaseId?",
    enabled: true,
    component: RentCaseFormPage,
  },
  {
    key: "rentcase-create-form-page-route",
    title: "RentCase Creation",
    path: "/users/newrentcase",
    enabled: true,
    component: RentCaseCreationFormPage,
  }
];